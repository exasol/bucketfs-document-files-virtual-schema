package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.GenericUdfCallHandler.*;
import static com.exasol.adapter.document.files.BucketfsDocumentFilesAdapter.ADAPTER_NAME;
import static com.exasol.matcher.ResultSetStructureMatcher.table;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.*;
import java.nio.file.Path;
import java.sql.*;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import org.junit.jupiter.api.*;

import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.dbbuilder.dialects.DatabaseObject;
import com.exasol.dbbuilder.dialects.exasol.*;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.exasoltestsetup.ExasolTestSetup;
import com.exasol.exasoltestsetup.testcontainers.ExasolTestcontainerTestSetup;
import com.exasol.udfdebugging.UdfTestSetup;

@Tag("integration")
class BucketfsDocumentFilesAdapterIT extends AbstractDocumentFilesAdapterIT {
    private static final String ADAPTER_JAR = "document-files-virtual-schema-dist-8.0.4-bucketfs-2.0.3.jar";
    private static final ExasolTestSetup EXASOL = new ExasolTestcontainerTestSetup();
    private static final String BUCKETS_BFSDEFAULT_DEFAULT = "/buckets/bfsdefault/default/";
    private static UdfTestSetup udfTestSetup;
    private static ExasolObjectFactory testDbBuilder;

    private static Connection connection;
    private static Statement statement;
    private static ConnectionDefinition connectionDefinition;
    private static AdapterScript adapterScript;
    private final List<DatabaseObject> createdObjects = new LinkedList<>();

    @BeforeAll
    static void beforeAll() throws Exception {
        connection = EXASOL.createConnection();
        statement = connection.createStatement();
        udfTestSetup = new UdfTestSetup(EXASOL, connection);
        final List<String> jvmOptions = new ArrayList<>(Arrays.asList(udfTestSetup.getJvmOptions()));
        jvmOptions.add("-Xmx500m");
        testDbBuilder = new ExasolObjectFactory(connection,
                ExasolObjectConfiguration.builder().withJvmOptions(jvmOptions.toArray(String[]::new)).build());
        final ExasolSchema adapterSchema = testDbBuilder.createSchema("ADAPTER");
        adapterScript = createAdapterScript(adapterSchema);
        createUdf(adapterSchema);
        connectionDefinition = createConnectionDefinition();
    }

    @AfterAll
    static void afterAll() throws SQLException {
        udfTestSetup.close();
        statement.close();
        connection.close();
    }

    @Override
    protected Statement getStatement() {
        return statement;
    }

    private static ConnectionDefinition createConnectionDefinition() {
        return testDbBuilder.createConnectionDefinition("CONNECTION", "", "", "{}");
    }

    private static AdapterScript createAdapterScript(final ExasolSchema adapterSchema)
            throws BucketAccessException, TimeoutException, FileNotFoundException {
        EXASOL.getDefaultBucket().uploadFile(Path.of("target", ADAPTER_JAR), ADAPTER_JAR);
        return adapterSchema.createAdapterScriptBuilder("BUCKETFS_ADAPTER")
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", BUCKETS_BFSDEFAULT_DEFAULT + ADAPTER_JAR)
                .language(AdapterScript.Language.JAVA).build();
    }

    private static void createUdf(final ExasolSchema adapterSchema) {
        adapterSchema.createUdfBuilder("IMPORT_FROM_BUCKETFS_DOCUMENT_FILES") //
                .language(UdfScript.Language.JAVA) //
                .inputType(UdfScript.InputType.SET) //
                .parameter(PARAMETER_DOCUMENT_FETCHER, "VARCHAR(2000000)") //
                .parameter(PARAMETER_SCHEMA_MAPPING_REQUEST, "VARCHAR(2000000)") //
                .parameter(PARAMETER_CONNECTION_NAME, "VARCHAR(500)") //
                .emits() //
                .bucketFsContent(UdfEntryPoint.class.getName(), BUCKETS_BFSDEFAULT_DEFAULT + ADAPTER_JAR) //
                .build();
    }

    @AfterEach
    void afterEach() {
        for (final DatabaseObject createdObject : this.createdObjects) {
            createdObject.drop();
        }
        this.createdObjects.clear();
    }

    @Override
    protected void uploadDataFile(final Supplier<InputStream> resource, final String resourceName) {
        try {
            EXASOL.getDefaultBucket().uploadInputStream(resource, resourceName);
        } catch (final BucketAccessException | TimeoutException exception) {
            throw new IllegalStateException("Failed to upload test-file to BucketFS.", exception);
        }
    }

    @Override
    protected void uploadDataFile(final Path path, final String resourceName) {
        try {
            EXASOL.getDefaultBucket().uploadFile(path, resourceName);
        } catch (final BucketAccessException | TimeoutException | FileNotFoundException exception) {
            throw new IllegalStateException("Failed to upload test-file to BucketFS.", exception);
        }
    }

    @Override
    protected void createVirtualSchema(final String schemaName, final String mapping) {
        this.createdObjects.add(testDbBuilder//
                .createVirtualSchemaBuilder(schemaName)//
                .connectionDefinition(connectionDefinition)//
                .adapterScript(adapterScript)//
                .dialectName(ADAPTER_NAME)//
                .properties(Map.of("MAPPING", mapping, "MAX_PARALLEL_UDFS", "1"))//
                .build());
    }

    @Override
    @Test
    public void testReadJson() throws SQLException, IOException {
        // Override to verify source references column
        createJsonVirtualSchema();
        final ResultSet result = getStatement()
                .executeQuery("SELECT ID, SOURCE_REFERENCE FROM TEST.BOOKS ORDER BY ID ASC;");
        final String expectedPrefix = "/bfsdefault/default/";
        assertThat(result, table().row("book-1", allOf(startsWith(expectedPrefix), endsWith("/testData-1.json")))
                .row("book-2", allOf(startsWith(expectedPrefix), endsWith("/testData-2.json"))).matches());
    }
}
