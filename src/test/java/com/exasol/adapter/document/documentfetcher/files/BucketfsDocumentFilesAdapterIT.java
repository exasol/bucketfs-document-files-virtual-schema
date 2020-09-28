package com.exasol.adapter.document.documentfetcher.files;

import static com.exasol.adapter.document.files.BucketfsDocumentFilesAdapter.ADAPTER_NAME;
import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

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

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;

@Tag("integration")
@Testcontainers
class BucketfsDocumentFilesAdapterIT {
    private static final String TEST_SCHEMA = "TEST_SCHEMA";
    private static final String ADAPTER_JAR = "document-files-virtual-schema-dist-0.2.0-SNAPSHOT-bucketfs-0.1.0.jar";
    private static final Logger LOGGER = LoggerFactory.getLogger(BucketfsDocumentFilesAdapterIT.class);
    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> container = new ExasolContainer<>()
            .withLogConsumer(new Slf4jLogConsumer(LOGGER)).withReuse(true);
    @TempDir
    static File tempDir;
    private static Connection connection;
    private static Statement statement;

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

    @AfterEach
    void afterEach() {
        container.purgeDatabase();
    }

    @Test
    void testReadJson()
            throws SQLException, InterruptedException, BucketAccessException, TimeoutException, IOException {
        final BucketfsVsExasolTestDatabaseBuilder filesVsExasolTestDatabaseBuilder = new BucketfsVsExasolTestDatabaseBuilder(
                container, ADAPTER_JAR);
        final Path mappingFile = saveResourceToFile("mapJsonFile.json");
        filesVsExasolTestDatabaseBuilder.createVirtualSchema(TEST_SCHEMA, mappingFile, ADAPTER_NAME);
        uploadResource("testData-1.json");
        uploadResource("testData-2.json");
        final ResultSet result = statement.executeQuery("SELECT ID FROM " + TEST_SCHEMA + ".BOOKS;");
        assertThat(result, table("VARCHAR").row("book-1").row("book-2").matches());
    }

    private void uploadResource(final String s)
            throws IOException, InterruptedException, BucketAccessException, TimeoutException {
        final Path testFile = saveResourceToFile(s);
        container.getDefaultBucket().uploadFile(testFile, s);
    }

    @Test
    void testReadJsonLines()
            throws IOException, InterruptedException, BucketAccessException, TimeoutException, SQLException {
        final BucketfsVsExasolTestDatabaseBuilder filesVsExasolTestDatabaseBuilder = new BucketfsVsExasolTestDatabaseBuilder(
                container, ADAPTER_JAR);
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
                .row("number", "1.23")//
                .row("null", equalTo(null))//
                .row("string", "test")//
                .row("true", "true")//
                .row("false", "false")//
                .matches());
    }

    @Test
    void testJsonDataTypesAsDecimal()
            throws InterruptedException, SQLException, TimeoutException, BucketAccessException, IOException {
        final ResultSet result = getDataTypesTestResult("mapDataTypesToDecimal.json");
        assertThat(result, table("VARCHAR", "DECIMAL")//
                .row("number", 1.23)//
                .row("null", equalTo(null))//
                .row("string", equalTo(null))//
                .row("true", equalTo(null))//
                .row("false", equalTo(null))//
                .matchesFuzzily());
    }

    @Test
    void testJsonDataTypesAsJson()
            throws InterruptedException, SQLException, TimeoutException, BucketAccessException, IOException {
        final ResultSet result = getDataTypesTestResult("mapDataTypesToJson.json");
        assertThat(result, table("VARCHAR", "VARCHAR")//
                .row("number", "1.23")//
                .row("null", "null")//
                .row("string", "\"test\"")//
                .row("true", "true")//
                .row("false", "false")//
                .matches());
    }

    private ResultSet getDataTypesTestResult(final String mappingFileName)
            throws SQLException, InterruptedException, BucketAccessException, TimeoutException, IOException {
        final BucketfsVsExasolTestDatabaseBuilder filesVsExasolTestDatabaseBuilder = new BucketfsVsExasolTestDatabaseBuilder(
                container, ADAPTER_JAR);
        final Path mappingFile = saveResourceToFile(mappingFileName);
        filesVsExasolTestDatabaseBuilder.createVirtualSchema(TEST_SCHEMA, mappingFile, ADAPTER_NAME);
        uploadResource("dataTypeTests.jsonl");
        return statement.executeQuery("SELECT * FROM " + TEST_SCHEMA + ".DATA_TYPES;");
    }
}
