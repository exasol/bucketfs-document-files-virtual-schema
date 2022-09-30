package com.exasol.adapter.document.documentfetcher.files;

import java.nio.file.Path;

import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * {@link FileLoader} for BucketFS.
 */
class BucketfsFileFinder extends AbstractLocalFileFinder {

    /**
     * Create a new instance of {@link BucketfsFileFinder}.
     *
     * @param filePattern files to load
     */
    public BucketfsFileFinder(final StringFilter filePattern) {
        super(new ExecutorServiceFactory(), Path.of("/buckets"), filePattern);
    }
}
