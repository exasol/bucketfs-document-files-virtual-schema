package com.exasol.adapter.document.files;

import com.exasol.adapter.document.documentfetcher.files.BucketFsFileLoaderFactory;
import com.exasol.adapter.document.documentfetcher.files.FileLoaderFactory;

/**
 * This class is the entry point for the BucketFS files Virtual Schema.
 */
public class BucketfsDocumentFilesAdapter extends DocumentFilesAdapter {
    public static final String ADAPTER_NAME = "BUCKETFS_DOCUMENT_FILES";

    @Override
    protected FileLoaderFactory getFileLoaderFactory() {
        return new BucketFsFileLoaderFactory();
    }

    @Override
    protected String getAdapterName() {
        return ADAPTER_NAME;
    }
}
