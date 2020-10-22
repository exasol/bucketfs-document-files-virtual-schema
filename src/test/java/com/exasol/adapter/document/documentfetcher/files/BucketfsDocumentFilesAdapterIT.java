package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeoutException;

import static com.exasol.adapter.document.files.BucketfsDocumentFilesAdapter.ADAPTER_NAME;
import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static com.exasol.udfdebugging.PushDownTesting.getPushDownSql;
import static com.exasol.udfdebugging.PushDownTesting.getSelectionThatIsSentToTheAdapter;
import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Tag("integration")
@Testcontainers
class BucketfsDocumentFilesAdapterIT {
    private static final String TEST_SCHEMA = "TEST_SCHEMA";
    private static final String ADAPTER_JAR = "document-files-virtual-schema-dist-0.3.0-SNAPSHOT-bucketfs-0.1.0.jar";
    private static final Logger LOGGER = LoggerFactory.getLogger(BucketfsDocumentFilesAdapterIT.class);
    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> container = new ExasolContainer<>("7.0.2")
            .withLogConsumer(new Slf4jLogConsumer(LOGGER)).withReuse(true);
    @TempDir
    static File tempDir;
    private static Connection connection;
    private static Statement statement;
    private BucketfsVsExasolTestDatabaseBuilder filesVsExasolTestDatabaseBuilder;

    @BeforeAll
    static void beforeAll() throws Exception {
        connection = container.createConnectionForUser(container.getUsername(), container.getPassword());
        statement = connection.createStatement();
    }

    @AfterAll
    static void afterAll() throws SQLException {
        statement.close();
        connection.close();
    }

    private static Path saveResourceToFile(final String resource) throws IOException {
        final InputStream inputStream = BucketfsDocumentFilesAdapterIT.class.getClassLoader()
                .getResourceAsStream(resource);
        final Path tempFile = File.createTempFile("resource", "", tempDir).toPath();
        Files.copy(inputStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
        return tempFile;
    }

    @BeforeEach
    void beforeEach() throws InterruptedException, SQLException, TimeoutException, BucketAccessException {
        this.filesVsExasolTestDatabaseBuilder = new BucketfsVsExasolTestDatabaseBuilder(
                container, ADAPTER_JAR);
    }

    @AfterEach
    void afterEach() {
        container.purgeDatabase();
    }

    @Test
    void testReadJson()
            throws SQLException, InterruptedException, BucketAccessException, TimeoutException, IOException {
        createJsonVirtualSchema();
        final ResultSet result = statement
                .executeQuery("SELECT ID, SOURCE_REFERENCE FROM " + TEST_SCHEMA + ".BOOKS ORDER BY ID ASC;");
        assertThat(result, table().row("book-1", "testData-1.json").row("book-2", "testData-2.json").matches());
    }

    private void uploadResource(final String s)
            throws IOException, InterruptedException, BucketAccessException, TimeoutException {
        final Path testFile = saveResourceToFile(s);
        container.getDefaultBucket().uploadFile(testFile, s);
    }

    @Test
    void testReadJsonLines()
            throws IOException, InterruptedException, BucketAccessException, TimeoutException, SQLException {
        final Path mappingFile = saveResourceToFile("mapJsonLinesFile.json");
        filesVsExasolTestDatabaseBuilder.createVirtualSchema(TEST_SCHEMA, mappingFile, ADAPTER_NAME);
        uploadResource("test.jsonl");

        final ResultSet result = statement.executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
        assertThat(result, table().row("book-1").row("book-2").matches());
    }

    @Test
    void testJsonDataTypesAsVarcharColumn()
            throws InterruptedException, SQLException, TimeoutException, BucketAccessException, IOException {
        final ResultSet result = getDataTypesTestResult("mapDataTypesToVarchar.json");
        assertThat(result, table("VARCHAR", "VARCHAR")//
                .row("false", "false")//
                .row("null", equalTo(null))//
                .row("number", "1.23")//
                .row("string", "test")//
                .row("true", "true")//
                .matches());
    }

    @Test
    void testJsonDataTypesAsDecimal()
            throws InterruptedException, SQLException, TimeoutException, BucketAccessException, IOException {
        final ResultSet result = getDataTypesTestResult("mapDataTypesToDecimal.json");
        assertThat(result, table("VARCHAR", "DECIMAL")//
                .row("false", equalTo(null))//
                .row("null", equalTo(null))//
                .row("number", 1.23)//
                .row("string", equalTo(null))//
                .row("true", equalTo(null))//
                .matchesFuzzily());
    }

    @Test
    void testJsonDataTypesAsJson()
            throws InterruptedException, SQLException, TimeoutException, BucketAccessException, IOException {
        final ResultSet result = getDataTypesTestResult("mapDataTypesToJson.json");
        assertThat(result, table("VARCHAR", "VARCHAR")//
                .row("false", "false")//
                .row("null", "null")//
                .row("number", "1.23")//
                .row("string", "\"test\"")//
                .row("true", "true")//
                .matches());
    }

    private ResultSet getDataTypesTestResult(final String mappingFileName)
            throws SQLException, InterruptedException, BucketAccessException, TimeoutException, IOException {
        final Path mappingFile = saveResourceToFile(mappingFileName);
        filesVsExasolTestDatabaseBuilder.createVirtualSchema(TEST_SCHEMA, mappingFile, ADAPTER_NAME);
        uploadResource("dataTypeTests.jsonl");
        return statement.executeQuery("SELECT * FROM " + TEST_SCHEMA + ".DATA_TYPES ORDER BY TYPE ASC;");
    }

    @Test
    void testFilterOnSourceReference() throws IOException, InterruptedException, BucketAccessException, TimeoutException, SQLException {
        createJsonVirtualSchema();
        String query = "SELECT ID FROM " + TEST_SCHEMA + ".BOOKS WHERE SOURCE_REFERENCE = 'testData-1.json'";
        try (
                final ResultSet result = statement
                        .executeQuery(query)) {
            assertAll(//
                    () -> assertThat(result, table().row("book-1").matches()),//
                    () -> assertThat(getPushDownSql(statement, query), endsWith("WHERE TRUE")),//no post selection
                    () -> assertThat(getSelectionThatIsSentToTheAdapter(statement, query), equalTo("BOOKS.SOURCE_REFERENCE='testData-1.json'"))//
            );
        }
    }

    @Test
    void testFilterWithOrOnSourceReference() throws IOException, InterruptedException, BucketAccessException, TimeoutException, SQLException {
        createJsonVirtualSchema();
        String query = "SELECT ID FROM " + TEST_SCHEMA + ".BOOKS WHERE SOURCE_REFERENCE = 'testData-1.json' OR SOURCE_REFERENCE = 'testData-2.json' ORDER BY SOURCE_REFERENCE ASC";
        assertAll(//
                () -> assertThat(statement.executeQuery(query), table().row("book-1").row("book-2").matches()),//
                () -> assertThat(getPushDownSql(statement, query), endsWith("WHERE TRUE")),//no post selection
                () -> assertThat(getSelectionThatIsSentToTheAdapter(statement, query), equalTo("BOOKS.SOURCE_REFERENCE='testData-1.json' OR BOOKS.SOURCE_REFERENCE='testData-2.json'"))//
        );
    }

    @Test
    void testFilterOnSourceReferenceForNonExisting() throws IOException, InterruptedException, BucketAccessException, TimeoutException, SQLException {
        createJsonVirtualSchema();
        String query = "SELECT ID FROM " + TEST_SCHEMA + ".BOOKS WHERE SOURCE_REFERENCE = 'UNKNOWN.json'";
        try (final ResultSet result = statement.executeQuery(query)) {
            assertAll(//
                    () -> assertThat(getSelectionThatIsSentToTheAdapter(statement, query), equalTo("BOOKS.SOURCE_REFERENCE='UNKNOWN.json'")),
                    () -> assertThat(result, table("VARCHAR").matches()),//
                    () -> assertThat(getPushDownSql(statement, query), endsWith("WHERE NOT(TRUE)"))
            );
        }
    }

    @Test
    void testFilterOnSourceReferenceUsingLike() throws IOException, InterruptedException, BucketAccessException, TimeoutException, SQLException {
        createJsonVirtualSchema();
        String query = "SELECT ID FROM " + TEST_SCHEMA + ".BOOKS WHERE SOURCE_REFERENCE LIKE '%1.json'";
        try (final ResultSet result = statement.executeQuery(query)) {
            assertAll(//
                    () -> assertThat(getSelectionThatIsSentToTheAdapter(statement, query), equalTo("BOOKS.SOURCE_REFERENCE LIKE '%1.json'")),
                    () -> assertThat(result, table().row("book-1").matches()),//
                    () -> assertThat(getPushDownSql(statement, query), endsWith("WHERE TRUE"))//no post selection
            );
        }
    }

    private void createJsonVirtualSchema() throws SQLException, InterruptedException, BucketAccessException, TimeoutException, IOException {
        final Path mappingFile = saveResourceToFile("mapJsonFile.json");
        this.filesVsExasolTestDatabaseBuilder.createVirtualSchema(TEST_SCHEMA, mappingFile, ADAPTER_NAME);
        uploadResource("testData-1.json");
        uploadResource("testData-2.json");
    }
}
