package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.adapter.document.files.AbstractDocumentFilesAdapterIT;
import com.exasol.bucketfs.BucketAccessException;
import com.exasol.containers.ExasolContainer;
import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

@Tag("integration")
@Testcontainers
class BucketfsDocumentFilesAdapterIT extends AbstractDocumentFilesAdapterIT {
    private static final String ADAPTER_JAR = "document-files-virtual-schema-dist-1.0.0-bucketfs-0.2.0.jar";

    @Container
    private static final ExasolContainer<? extends ExasolContainer<?>> container = new ExasolContainer<>().withReuse(true);

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

    @BeforeEach
    void beforeEach() throws InterruptedException, SQLException, TimeoutException, BucketAccessException {
        this.filesVsExasolTestDatabaseBuilder = new BucketfsVsExasolTestDatabaseBuilder(
                container, ADAPTER_JAR);
    }

    @AfterEach
    void afterEach() {
        container.purgeDatabase();
    }

    @Override
    protected Statement getStatement() {
        return statement;
    }

    @Override
    protected void uploadDataFile(Supplier<InputStream> resource, String resourceName) {
        try {
            BucketfsDocumentFilesAdapterIT.container.getDefaultBucket().uploadInputStream(resource, resourceName);
        } catch (InterruptedException | BucketAccessException | TimeoutException exception) {
            throw new IllegalStateException("Failed to upload test-file to BucketFS.", exception);
        }
    }

    @Override
    protected void createVirtualSchema(String schemaName, Supplier<InputStream> mapping) {
        try {
            filesVsExasolTestDatabaseBuilder.createVirtualSchema(schemaName, mapping);
        } catch (InterruptedException | BucketAccessException | TimeoutException exception) {
            throw new IllegalStateException("Failed to create virtual schema.", exception);
        }
    }
}
