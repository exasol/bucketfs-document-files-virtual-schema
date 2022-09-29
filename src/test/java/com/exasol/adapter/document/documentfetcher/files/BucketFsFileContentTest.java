package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

class BucketFsFileContentTest {

    private static final String FILE_CONTENT = "file content";

    @TempDir
    Path tempDir;

    private ExecutorServiceFactory executorServiceFactory;

    @BeforeEach
    void setup() {
        this.executorServiceFactory = new ExecutorServiceFactory();
    }

    @AfterEach
    void close() {
        this.executorServiceFactory.close();
        this.executorServiceFactory = null;
    }

    @Test
    void getInputStreamReturnsFile() throws IOException {
        final Path file = this.tempDir.resolve("file");
        Files.writeString(file, FILE_CONTENT);
        final String fromBucket = read(create(file));
        assertThat(fromBucket, equalTo(FILE_CONTENT));
    }

    private String read(final BucketFsFileContent content) throws IOException {
        try (InputStream is = content.getInputStream()) {
            return new String(is.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    @Test
    void getInputStreamFailsForMissingFile() {
        final Path file = this.tempDir.resolve("non-existing-file");
        final BucketFsFileContent content = create(file);
        final IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> content.getInputStream());
        assertThat(exception.getMessage(), equalTo("F-BFSVS-6: Could not open '" + file
                + "'. This is an internal error that should not happen. Please report it by opening a GitHub issue."));
    }

    @Test
    void loadAsyncFailsForMissingFile() {
        final Path file = this.tempDir.resolve("non-existing-file");
        final Future<byte[]> content = create(file).loadAsync();
        final ExecutionException exception = assertThrows(ExecutionException.class, () -> content.get());
        assertThat(exception.getMessage(), equalTo("java.lang.IllegalStateException: F-BFSVS-6: Could not open '" + file
                + "'. This is an internal error that should not happen. Please report it by opening a GitHub issue."));
    }

    @Test
    void loadAsyncReadsFileContent() throws IOException, InterruptedException, ExecutionException {
        final Path file = this.tempDir.resolve("file");
        Files.writeString(file, FILE_CONTENT);
        final Future<byte[]> content = create(file).loadAsync();
        final String readContent = new String(content.get(), StandardCharsets.UTF_8);
        assertThat(readContent, equalTo(FILE_CONTENT));
    }

    private BucketFsFileContent create(final Path filePath) {
        return new BucketFsFileContent(this.executorServiceFactory, filePath);
    }
}
