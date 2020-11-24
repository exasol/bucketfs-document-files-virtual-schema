package com.exasol.adapter.document.documentfetcher.files;


import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.files.stringfilter.matcher.Matcher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Abstract basis for {@link FileLoader}s local files.
 *
 * @implNote This class is only used by the {@link BucketfsFileLoader}. It is introduced to support unit testing.
 */
abstract class AbstractLocalFileLoader implements FileLoader {
    private final StringFilter filePattern;
    private final SegmentMatcher segmentMatcher;
    private final Path baseDirectory;

    /**
     * Create a new instance of {@link AbstractLocalFileLoader}.
     *
     * @param filePattern        GLOB pattern for the file set to load
     * @param segmentDescription files to load
     */
    AbstractLocalFileLoader(final Path baseDirectory,
                            final StringFilter filePattern,
                            final SegmentDescription segmentDescription) {
        this.baseDirectory = baseDirectory;
        this.filePattern = filePattern;
        this.segmentMatcher = new SegmentMatcher(segmentDescription);
    }

    @Override
    public Stream<InputStreamWithResourceName> loadFiles() {
        final Path nonGlobPath = getPrefixPathSafely();
        try {
            final Matcher matcher = filePattern.getDirectoryAwareMatcher(FileSystems.getDefault().getSeparator());
            return Files.walk(nonGlobPath).filter(path -> matcher.matches(this.relativize(path))).filter(this::isFilePartOfThisSegment)
                    .map(filePath -> new InputStreamWithResourceName(getInputStream(filePath), this.relativize(filePath)));
        } catch (final IOException exception) {
            throw getCouldNotOpenException(nonGlobPath, exception);
        }
    }

    public String relativize(Path path) {
        return "/" + baseDirectory.relativize(path).toString();
    }

    private Path getPrefixPathSafely() {
        final Path path = getPrefixPath();
        try {
            if (!path.toFile().getCanonicalPath().startsWith(baseDirectory.toString())) {
                throw new IllegalArgumentException("E-BFSVS-2 The path '" + path + "' is outside of BucketFS. Please make sure, that you do not use '../' to leave BucketFS.");
            } else {
                return path;
            }
        } catch (IOException exception) {
            throw getCouldNotOpenException(path, exception);
        }
    }

    private IllegalArgumentException getCouldNotOpenException(Path path, Exception cause) {
        return new IllegalArgumentException("E-BFSVS-1 Could not open '" + path + "'. " +
                "Please make sure that you defined the correct path in the CONNECTION and the mapping definition.", cause);
    }

    private Path getPrefixPath() {
        final String staticPrefix = filePattern.getStaticPrefix();
        validatePrefixStartsWithSlash(staticPrefix);
        final Path path = baseDirectory.resolve(staticPrefix.replaceFirst("/", ""));
        if (path.toFile().isDirectory()) {
            return path;
        } else {
            return path.getParent();
        }
    }

    private void validatePrefixStartsWithSlash(String staticPrefix) {
        if (!staticPrefix.startsWith("/")) {
            throw new IllegalArgumentException("E-BFDVS-3 Invalid path '" + staticPrefix + "'. The BucketFS path must have the format '/<bucket>/...'. Please add the trailing slash to the address in the CONNECTION.");
        }
    }

    private boolean isFilePartOfThisSegment(final Path path) {
        return this.segmentMatcher.matches(path.toString());
    }

    private InputStream getInputStream(final Path path) {
        try {
            return new FileInputStream(path.toFile());
        } catch (final FileNotFoundException exception) {
            throw getCouldNotOpenException(path, exception);
        }
    }
}
