package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AbstractLocalFileLoaderTest {

    @Test
    void testInjection() {
        final FileLoaderStub loaderStub = new FileLoaderStub(Path.of("/buckets/"), WildcardExpression.fromGlob("/../etc/passwd*.json"), SegmentDescription.NO_SEGMENTATION);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> loaderStub.loadFiles());
        assertThat(exception.getMessage(), containsString("E-BFSVS-2"));
    }

    @Test
    void testNonExistentFile() {
        final FileLoaderStub loaderStub = new FileLoaderStub(Path.of("/buckets/"), WildcardExpression.fromGlob("/unknown.json"), SegmentDescription.NO_SEGMENTATION);
        final IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> loaderStub.loadFiles());
        assertThat(exception.getMessage(), equalTo("E-BFSVS-1 Could not open '/buckets'. Please make sure that you defined the correct path in the CONNECTION and the mapping definition."));
    }

    @Test
    void testLoadWithGlob(@TempDir final Path tempDir) throws IOException {
        final Path testFile1 = createTestFile(tempDir, "testFile", "file-1");
        final Path testFile2 = createTestFile(tempDir, "testFile", "file-2");
        createTestFile(tempDir, "otherFiles", "file-3");
        final FileLoaderStub fileLoader = new FileLoaderStub(tempDir, WildcardExpression.fromGlob("/testFile*.json"),
                SegmentDescription.NO_SEGMENTATION);
        final List<InputStreamWithResourceName> result = fileLoader.loadFiles()
                .collect(Collectors.toList());
        final List<String> firstLines = readFirstLineFromStreams(result.stream().map(InputStreamWithResourceName::getInputStream).collect(Collectors.toList()));
        final List<String> resourceNames = result.stream().map(InputStreamWithResourceName::getResourceName).collect(Collectors.toList());
        assertAll(//
                () -> assertThat(firstLines, containsInAnyOrder("file-1", "file-2")),
                () -> assertThat(resourceNames, containsInAnyOrder("/" + testFile1.getFileName().toString(), "/" + testFile2.getFileName().toString()))//
        );
    }

    @NotNull
    private List<String> readFirstLineFromStreams(final List<InputStream> inputStreams) throws IOException {
        final List<String> result = new ArrayList<>(2);
        for (final InputStream inputStream : inputStreams) {
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            result.add(reader.readLine());
            reader.close();
            inputStream.close();
        }
        return result;
    }

    private Path createTestFile(final Path tempDir, final String name, final String content) throws IOException {
        final Path testFile = Files.createTempFile(tempDir, name, ".json");
        final FileWriter fileWriter = new FileWriter(testFile.toFile());
        fileWriter.write(content);
        fileWriter.flush();
        fileWriter.close();
        return testFile;
    }

    private static class FileLoaderStub extends AbstractLocalFileLoader {

        public FileLoaderStub(final Path baseDirectory, final StringFilter filePattern,
                              final SegmentDescription segmentDescription) {
            super(baseDirectory, filePattern, segmentDescription);
        }
    }
}