package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.ExaConnectionInformation;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Factory for {@link FileLoader}
 */
public class BucketFsFileLoaderFactory implements FileLoaderFactory {
    private static final long serialVersionUID = 3468167710047185881L;

    @Override
    public FileLoader getLoader(final StringFilter filePattern, final ExaConnectionInformation connectionInformation) {
        return new BucketfsFileLoader(filePattern);
    }
}
