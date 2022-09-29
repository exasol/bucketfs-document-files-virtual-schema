package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.files.stringfilter.StringFilter;

/**
 * Factory for {@link RemoteFileFinder}
 */
public class BucketFsFileFinderFactory implements FileFinderFactory {
    private static final long serialVersionUID = 3468167710047185881L;

    @Override
    public RemoteFileFinder getFinder(final StringFilter filePattern,
            final ConnectionPropertiesReader connectionPropertyReader) {
        return new BucketfsFileFinder(filePattern);
    }

    @Override
    public String getUserGuideUrl() {
        return BucketFsVsConstants.USER_GUIDE_URL;
    }
}
