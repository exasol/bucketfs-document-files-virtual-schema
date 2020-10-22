package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.adapter.document.files.stringfilter.StringFilter;

import java.nio.file.Path;

/**
 * {@link FileLoader} for BucketFS.
 */
class BucketfsFileLoader extends AbstractLocalFileLoader {

    /**
     * Create a new instance of {@link BucketfsFileLoader}.
     *
     * @param filePattern        files to load
     * @param segmentDescription segmentation for parallel execution
     */
    public BucketfsFileLoader(final StringFilter filePattern,
                              final SegmentDescription segmentDescription) {
        super(Path.of("/buckets"), filePattern, segmentDescription);
    }
}
