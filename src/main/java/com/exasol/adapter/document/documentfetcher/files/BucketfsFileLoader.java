package com.exasol.adapter.document.documentfetcher.files;

import java.nio.file.Path;

import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * {@link FileLoader} for BucketFS.
 */
class BucketfsFileLoader extends AbstractLocalFileLoader {

    /**
     * Create a new instance of {@link BucketfsFileLoader}.
     *
     * @param filePattern files to load
     */
    public BucketfsFileLoader(final StringFilter filePattern) {
        super(Path.of("/buckets"), filePattern);
    }
}
