package com.exasol.adapter.document.documentfetcher.files;

import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.Future;

import com.exasol.errorreporting.ExaError;

class BucketFsFileContent implements RemoteFileContent {

    private final ExecutorServiceFactory executorServiceFactory;
    private final Path filePath;

    BucketFsFileContent(final ExecutorServiceFactory executorServiceFactory, final Path filePath) {
        this.executorServiceFactory = executorServiceFactory;
        this.filePath = filePath;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(this.filePath.toFile());
        } catch (final FileNotFoundException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-VSBFS-6")
                    .message("Could not open {{file}}.", this.filePath).ticketMitigation().toString());
        }
    }

    @Override
    public Future<byte[]> loadAsync() {
        return executorServiceFactory.getExecutorService().submit(this::readAllBytes);
    }

    private byte[] readAllBytes() {
        try (InputStream stream = getInputStream()) {
            return stream.readAllBytes();
        } catch (final IOException exception) {
            throw new UncheckedIOException(
                    ExaError.messageBuilder("F-VSBFS-8").message("Failed to read file").toString(), exception);
        }
    }
}
