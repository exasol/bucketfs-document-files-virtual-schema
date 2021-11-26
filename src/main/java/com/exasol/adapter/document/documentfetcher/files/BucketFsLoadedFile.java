package com.exasol.adapter.document.documentfetcher.files;

import java.io.*;
import java.nio.file.Path;

import com.exasol.adapter.document.documentfetcher.files.randomaccessinputstream.*;
import com.exasol.errorreporting.ExaError;

class BucketFsRemoteFile extends RemoteFile {
    private static final int SIZE_1_MB = 1000000;
    private final Path filePath;

    /**
     * Create a new instance of {@link BucketFsRemoteFile}.
     *
     * @param resourceName description of the file e.g. file name; used for error messages
     */
    BucketFsRemoteFile(final Path filePath, final String resourceName) {
        super(resourceName);
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
    public RandomAccessInputStream getRandomAccessInputStream() {
        try {
            return new RandomAccessInputStreamCache(new FileRandomAccessInputStream(this.filePath.toFile()), SIZE_1_MB);
        } catch (final FileNotFoundException e) {
            throw new IllegalStateException(ExaError.messageBuilder("F-BFSVS-4")
                    .message("Could not open {{file}}'.", this.filePath).ticketMitigation().toString());
        }
    }
}
