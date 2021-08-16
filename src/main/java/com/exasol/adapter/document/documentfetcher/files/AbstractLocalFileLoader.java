package com.exasol.adapter.document.documentfetcher.files;

import java.io.IOException;
import java.nio.file.*;
import java.util.Iterator;
import java.util.stream.Stream;

import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.files.stringfilter.matcher.Matcher;
import com.exasol.adapter.document.iterators.AfterAllCallbackIterator;
import com.exasol.errorreporting.ExaError;

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
    AbstractLocalFileLoader(final Path baseDirectory, final StringFilter filePattern,
            final SegmentDescription segmentDescription) {
        this.baseDirectory = baseDirectory;
        this.filePattern = filePattern;
        this.segmentMatcher = new SegmentMatcher(segmentDescription);
    }

    @Override
    public Iterator<LoadedFile> loadFiles() {
        final Path nonGlobPath = getPrefixPathSafely();
        final Matcher matcher = this.filePattern.getDirectoryAwareMatcher(FileSystems.getDefault().getSeparator());
        final Stream<Path> filesStream = walkFiles(nonGlobPath);
        final Iterator<LoadedFile> iterator = filesStream
                .filter(path -> matcher.matches(this.relativize(path)) && isFilePartOfThisSegment(path))
                .map(path -> (LoadedFile) new BucketFsLoadedFile(path, relativize(path))).iterator();
        return new AfterAllCallbackIterator<>(iterator, filesStream::close);
    }

    private Stream<Path> walkFiles(final Path nonGlobPath) {
        try {
            return Files.walk(nonGlobPath);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-VFSVS-5")
                    .message("Failed to list / open file from BucketFs.").ticketMitigation().toString(), exception);
        }
    }

    public String relativize(final Path path) {
        return "/" + this.baseDirectory.relativize(path);
    }

    private Path getPrefixPathSafely() {
        final Path path = getPrefixPath();
        try {
            if (!path.toFile().getCanonicalPath().startsWith(this.baseDirectory.toString())) {
                throw new IllegalArgumentException(ExaError.messageBuilder("E-BFSVS-2")
                        .message("The path {{path}} is outside of BucketFS.", path)
                        .mitigation("Please make sure, that you do not use '../' to leave BucketFS.").toString());
            } else {
                return path;
            }
        } catch (final IOException exception) {
            throw getCouldNotOpenException(path, exception);
        }
    }

    private IllegalArgumentException getCouldNotOpenException(final Path path, final Exception cause) {
        return new IllegalArgumentException(ExaError.messageBuilder("E-BFSVS-1")
                .message("Could not open {{path}}.", path)
                .mitigation(
                        "Please make sure that you defined the correct path in the CONNECTION and the mapping definition.")
                .toString(), cause);
    }

    private Path getPrefixPath() {
        final String staticPrefix = this.filePattern.getStaticPrefix();
        validatePrefixStartsWithSlash(staticPrefix);
        final Path path = this.baseDirectory.resolve(staticPrefix.replaceFirst("/", ""));
        if (path.toFile().isDirectory()) {
            return path;
        } else {
            return path.getParent();
        }
    }

    private void validatePrefixStartsWithSlash(final String staticPrefix) {
        if (!staticPrefix.startsWith("/")) {
            throw new IllegalArgumentException(ExaError.messageBuilder("E-BFSVS-3")
                    .message("Invalid path {{path}}. The BucketFS path must have the format '/<bucket>/...'.",
                            staticPrefix)
                    .mitigation("Please add the trailing slash to the address in the CONNECTION.").toString());
        }
    }

    private boolean isFilePartOfThisSegment(final Path path) {
        return this.segmentMatcher.matches(path.toString());
    }
}
