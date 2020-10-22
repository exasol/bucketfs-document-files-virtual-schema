package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.adapter.document.UdfEntryPoint;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;
import com.exasol.dbbuilder.dialects.exasol.AdapterScript;
import com.exasol.dbbuilder.dialects.exasol.ConnectionDefinition;
import com.exasol.dbbuilder.dialects.exasol.ExasolObjectFactory;
import com.exasol.dbbuilder.dialects.exasol.ExasolSchema;
import com.github.dockerjava.api.model.ContainerNetwork;

import java.nio.file.Path;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import static com.exasol.adapter.document.UdfEntryPoint.*;
import static com.exasol.adapter.document.files.BucketfsDocumentFilesAdapter.ADAPTER_NAME;

public class BucketfsVsExasolTestDatabaseBuilder {
    public static final String BUCKETS_BFSDEFAULT_DEFAULT = "/buckets/bfsdefault/default/";
    public static final String DEBUGGER_PORT = "8000";
    private static final String FILES_ADAPTER = "FILES_ADAPTER";
    private static ExasolObjectFactory exasolObjectFactory;
    private final ExasolContainer<? extends ExasolContainer<?>> testContainer;
    private final AdapterScript adapterScript;
    private final String jarName;
    private final ConnectionDefinition connection;
    private String mappingInBucketfs;

    public BucketfsVsExasolTestDatabaseBuilder(final ExasolContainer<? extends ExasolContainer<?>> testContainer,
            final String jarName) throws SQLException, InterruptedException, BucketAccessException, TimeoutException {
        this.jarName = jarName;
        this.testContainer = testContainer;
        exasolObjectFactory = new ExasolObjectFactory(testContainer.createConnection());
        this.adapterScript = createAdapterScript();
        createUdf();
        this.connection = createConnectionDefinition();
    }

    private String getTestHostIp() {
        final Map<String, ContainerNetwork> networks = this.testContainer.getContainerInfo().getNetworkSettings()
                .getNetworks();
        if (networks.size() == 0) {
            return null;
        }
        return networks.values().iterator().next().getGateway();
    }

    public void createVirtualSchema(final String name, final Path mapping, final String adapterName)
            throws InterruptedException, BucketAccessException, TimeoutException {
        this.mappingInBucketfs = "mapping.json";
        this.testContainer.getDefaultBucket().uploadFile(mapping, this.mappingInBucketfs);
        exasolObjectFactory//
                .createVirtualSchemaBuilder(name)//
                .connectionDefinition(this.connection)//
                .adapterScript(this.adapterScript)//
                .dialectName(adapterName)//
                .properties(Map.of("MAPPING", "/bfsdefault/default/" + this.mappingInBucketfs, "MAX_PARALLEL_UDFS", "1"))//
                .build();
    }

    private ConnectionDefinition createConnectionDefinition() {
        return exasolObjectFactory.createConnectionDefinition("CONNECTION", "/bfsdefault/default/", "", "");
    }

    private AdapterScript createAdapterScript() throws InterruptedException, BucketAccessException, TimeoutException {
        this.testContainer.getDefaultBucket().uploadFile(Path.of("target", this.jarName), this.jarName);
        final ExasolSchema adapterSchema = exasolObjectFactory.createSchema("ADAPTER");
        return adapterSchema.createAdapterScriptBuilder().name(FILES_ADAPTER)
                .bucketFsContent("com.exasol.adapter.RequestDispatcher", BUCKETS_BFSDEFAULT_DEFAULT + this.jarName)
                .language(AdapterScript.Language.JAVA).debuggerConnection(getTestHostIp() + ":" + DEBUGGER_PORT)
                .build();
    }

    // TODO refactor to use test-db-builder
    private void createUdf() throws SQLException {
        final StringBuilder statementBuilder = new StringBuilder(
                "CREATE OR REPLACE JAVA SET SCRIPT ADAPTER." + UDF_PREFIX + ADAPTER_NAME + "(" + PARAMETER_DATA_LOADER
                        + " VARCHAR(2000000), " + PARAMETER_SCHEMA_MAPPING_REQUEST + " VARCHAR(2000000), "
                        + PARAMETER_CONNECTION_NAME + " VARCHAR(500)) EMITS(...) AS\n");
        // statementBuilder.append(getDebuggerOptions(true));
        statementBuilder.append("    %scriptclass " + UdfEntryPoint.class.getName() + ";\n");
        statementBuilder.append("    %jar /buckets/bfsdefault/default/" + this.jarName + ";\n");
        //statementBuilder.append("    %jvmoption -agentlib:jdwp=transport=dt_socket,server=n,address=" + getTestHostIp() + ":8000;\n");
        statementBuilder.append("/");
        final String sql = statementBuilder.toString();
        this.testContainer.createConnectionForUser(this.testContainer.getUsername(), this.testContainer.getPassword())
                .createStatement().execute(sql);
    }
}
