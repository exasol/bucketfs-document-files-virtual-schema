package com.exasol.adapter.document.documentfetcher.files;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.Iterator;
import java.util.stream.Stream;

import com.exasol.adapter.document.files.stringfilter.StringFilter;
import com.exasol.adapter.document.files.stringfilter.matcher.Matcher;
import com.exasol.adapter.document.iterators.CloseableIterator;
import com.exasol.adapter.document.iterators.CloseableIteratorWrapper;
import com.exasol.errorreporting.ExaError;

/**
 * Abstract basis for {@link FileLoader}s local files.
 *
 * @implNote This class is only used by the {@link BucketfsFileFinder}. It is introduced to support unit testing.
 */
abstract class AbstractLocalFileFinder implements RemoteFileFinder {
    private final ExecutorServiceFactory executorServiceFactory;
    private final StringFilter filePattern;
    private final Path baseDirectory;

    /**
     * Create a new instance of {@link AbstractLocalFileFinder}.
     *
     * @param filePattern GLOB pattern for the file set to load
     */
    AbstractLocalFileFinder(final ExecutorServiceFactory executorServiceFactory, final Path baseDirectory,
            final StringFilter filePattern) {
        this.executorServiceFactory = executorServiceFactory;
        this.baseDirectory = baseDirectory;
        this.filePattern = filePattern;
    }

    @Override
    public CloseableIterator<RemoteFile> loadFiles() {
        final Path nonGlobPath = getPrefixPathSafely();
        final Matcher matcher = this.filePattern.getDirectoryAwareMatcher(FileSystems.getDefault().getSeparator());
        final Stream<Path> filesStream = walkFiles(nonGlobPath);
        final Iterator<RemoteFile> iterator = filesStream.filter(path -> matcher.matches(this.relativize(path)))
                .map(this::createRemoteFile).iterator();
        return new CloseableIteratorWrapper<>(iterator, filesStream::close);
    }

    private RemoteFile createRemoteFile(final Path path) {
        return new RemoteFile(relativize(path), getFileSize(path), getFileContent(path));
    }

    private RemoteFileContent getFileContent(final Path path) {
        return new BucketFsFileContent(this.executorServiceFactory, path);
    }

    private long getFileSize(final Path path) {
        try {
            return Files.size(path);
        } catch (final IOException exception) {
            throw new UncheckedIOException(ExaError.messageBuilder("E-VSBFS-7")
                    .message("Failed to get file size of file {{file}}", path).ticketMitigation().toString(),
                    exception);
        }
    }

    private Stream<Path> walkFiles(final Path nonGlobPath) {
        try {
            return Files.walk(nonGlobPath);
        } catch (final IOException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-VSBFS-5")
                    .message("Failed to list / open file from BucketFs.").ticketMitigation().toString(), exception);
        }
    }

    public String relativize(final Path path) {
        return "/" + this.baseDirectory.relativize(path);
    }

    private Path getPrefixPathSafely() {
        final Path path = getPrefixPath();
        try {
            final String canonicalPath = path.toFile().getCanonicalPath();
            final String canonicalBaseDirectory = this.baseDirectory.toFile().getCanonicalPath();
            if (!canonicalPath.startsWith(canonicalBaseDirectory)) {
                throw new IllegalArgumentException(ExaError.messageBuilder("E-VSBFS-2")
                        .message("The path {{path}} is outside of BucketFS {{basePath}}.", canonicalPath,
                                canonicalBaseDirectory)
                        .mitigation("Please make sure, that you do not use '../' to leave BucketFS.").toString());
            } else {
                return path;
            }
        } catch (final IOException exception) {
            throw getCouldNotOpenException(path, exception);
        }
    }

    private IllegalArgumentException getCouldNotOpenException(final Path path, final Exception cause) {
        return new IllegalArgumentException(ExaError.messageBuilder("E-VSBFS-1")
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
            throw new IllegalArgumentException(ExaError.messageBuilder("E-VSBFS-3")
                    .message("Invalid path {{path}}. The BucketFS path must have the format '/<bucket>/...'.",
                            staticPrefix)
                    .mitigation("Please add the trailing slash to the address in the CONNECTION.").toString());
        }
    }
}
