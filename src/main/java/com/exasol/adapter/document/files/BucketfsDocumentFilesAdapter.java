package com.exasol.adapter.document.files;

import com.exasol.adapter.document.documentfetcher.files.BucketFsFileFinderFactory;
import com.exasol.adapter.document.documentfetcher.files.FileFinderFactory;

/**
 * This class is the entry point for the BucketFS files Virtual Schema.
 */
public class BucketfsDocumentFilesAdapter extends DocumentFilesAdapter {

    public static final String ADAPTER_NAME = "BUCKETFS_DOCUMENT_FILES";

    public BucketfsDocumentFilesAdapter(final String adapterName, final FileFinderFactory fileFinderFactory) {
        super(ADAPTER_NAME, new BucketFsFileFinderFactory());
    }
}
