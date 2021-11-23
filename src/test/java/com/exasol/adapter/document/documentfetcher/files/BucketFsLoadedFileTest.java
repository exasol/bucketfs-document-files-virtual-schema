package com.exasol.adapter.document.documentfetcher.files;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class BucketFsRemoteFileTest {
    private static final String EXPECTED_CONTENT = "test123";
    @TempDir
    static Path tempDir;
    private static BucketFsRemoteFile remoteFile;

    @BeforeAll
    static void beforeAll() throws IOException {
        final Path testFile = tempDir.resolve("aFile.txt");
        Files.writeString(testFile, EXPECTED_CONTENT);
        remoteFile = new BucketFsRemoteFile(testFile, "test");
    }

    @Test
    void testGetInputStream() throws IOException {
        final String result = new String(remoteFile.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
        assertThat(result, equalTo(EXPECTED_CONTENT));
    }

    @Test
    void testGetRandomAccessInputStream() throws IOException {
        final String result = new String(remoteFile.getRandomAccessInputStream().readAllBytes(),
                StandardCharsets.UTF_8);
        assertThat(result, equalTo(EXPECTED_CONTENT));
    }
}