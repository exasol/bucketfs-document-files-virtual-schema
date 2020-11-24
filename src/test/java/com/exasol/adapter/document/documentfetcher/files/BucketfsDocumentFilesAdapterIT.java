package com.exasol.adapter.document.documentfetcher.files;

import static com.exasol.adapter.document.UdfEntryPoint.*;
import static com.exasol.adapter.document.files.BucketfsDocumentFilesAdapter.ADAPTER_NAME;

import java.io.InputStream;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.adapter.document.files.AbstractDocumentFilesAdapterIT;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;
import com.exasol.dbbuilder.dialects.DatabaseObject;
import com.exasol.dbbuilder.dialects.exasol.*;
import com.exasol.dbbuilder.dialects.exasol.udf.UdfScript;
import com.exasol.udfdebugging.UdfTestSetup;
import com.github.dockerjava.api.model.ContainerNetwork;

@Tag("integration")
@Testcontainers
class BucketfsDocumentFilesAdapterIT extends AbstractDocumentFilesAdapterIT {
    private static final String ADAPTER_JAR = "document-files-virtual-schema-dist-1.0.0-bucketfs-0.2.0.jar";

    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> EXASOL = new ExasolContainer<>().withReuse(true);
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
        connection = EXASOL.createConnectionForUser(EXASOL.getUsername(), EXASOL.getPassword());
        statement = connection.createStatement();
        udfTestSetup = new UdfTestSetup(getTestHostIp(), EXASOL.getDefaultBucket());
        testDbBuilder = new ExasolObjectFactory(EXASOL.createConnection(),
                ExasolObjectConfiguration.builder().withJvmOptions(udfTestSetup.getJvmOptions()).build());
        final ExasolSchema adapterSchema = testDbBuilder.createSchema("ADAPTER");
        adapterScript = createAdapterScript(adapterSchema);
        createUdf(adapterSchema);
        connectionDefinition = createConnectionDefinition();
    }

    @AfterAll
    static void afterAll() throws SQLException {
        statement.close();
        connection.close();
    }

    private static String getTestHostIp() {
        final Map<String, ContainerNetwork> networks = EXASOL.getContainerInfo().getNetworkSettings().getNetworks();
        if (networks.size() == 0) {
            return null;
        }
        return networks.values().iterator().next().getGateway();
    }

    @Override
    protected Statement getStatement() {
        return statement;
    }

    private static ConnectionDefinition createConnectionDefinition() {
        return testDbBuilder.createConnectionDefinition("CONNECTION", "/bfsdefault/default/", "", "");
    }

    private static AdapterScript createAdapterScript(final ExasolSchema adapterSchema)
            throws InterruptedException, BucketAccessException, TimeoutException {
        EXASOL.getDefaultBucket().uploadFile(Path.of("target", ADAPTER_JAR), ADAPTER_JAR);
        return adapterSchema.createAdapterScriptBuilder("BUCKETFS_ADAPTER")
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", BUCKETS_BFSDEFAULT_DEFAULT + ADAPTER_JAR)
                .language(AdapterScript.Language.JAVA).build();
    }

    private static void createUdf(final ExasolSchema adapterSchema) {
        adapterSchema.createUdfBuilder("IMPORT_FROM_BUCKETFS_DOCUMENT_FILES").language(UdfScript.Language.JAVA)
                .inputType(UdfScript.InputType.SET).parameter(PARAMETER_DATA_LOADER, "VARCHAR(2000000)")
                .parameter(PARAMETER_SCHEMA_MAPPING_REQUEST, "VARCHAR(2000000)")
                .parameter(PARAMETER_CONNECTION_NAME, "VARCHAR(500)").emits()
                .bucketFsContent(UdfEntryPoint.class.getName(), BUCKETS_BFSDEFAULT_DEFAULT + ADAPTER_JAR).build();
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
            BucketfsDocumentFilesAdapterIT.EXASOL.getDefaultBucket().uploadInputStream(resource, resourceName);
        } catch (final InterruptedException | BucketAccessException | TimeoutException exception) {
            throw new IllegalStateException("Failed to upload test-file to BucketFS.", exception);
        }
    }

    @Override
    protected void createVirtualSchema(final String schemaName, final Supplier<InputStream> mapping) {
        try {
            final String mappingInBucketfs = "mapping.json";
            EXASOL.getDefaultBucket().uploadInputStream(mapping, mappingInBucketfs);
            this.createdObjects.add(testDbBuilder//
                    .createVirtualSchemaBuilder(schemaName)//
                    .connectionDefinition(connectionDefinition)//
                    .adapterScript(adapterScript)//
                    .dialectName(ADAPTER_NAME)//
                    .properties(Map.of("MAPPING", "/bfsdefault/default/" + mappingInBucketfs, "MAX_PARALLEL_UDFS", "1"))//
                    .build());
        } catch (final InterruptedException | BucketAccessException | TimeoutException exception) {
            throw new IllegalStateException("Failed to create virtual schema.", exception);
        }
    }
}
