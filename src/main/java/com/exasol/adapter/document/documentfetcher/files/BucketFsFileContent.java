package com.exasol.adapter.document.documentfetcher.files;

import java.io.*;
import java.nio.file.Path;
import java.util.concurrent.Future;

import com.exasol.errorreporting.ExaError;

class BucketFsFileContent implements RemoteFileContent {

    private final Path filePath;

    BucketFsFileContent(final Path filePath) {
        this.filePath = filePath;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(this.filePath.toFile());
        } catch (final FileNotFoundException exception) {
            throw new IllegalStateException(ExaError.messageBuilder("F-BFSVS-6")
                    .message("Could not open {{file}}'.", this.filePath).ticketMitigation().toString());
        }
    }

    @Override
    public Future<byte[]> loadAsync() {
        throw new UnsupportedOperationException("blubb");
    }
}
