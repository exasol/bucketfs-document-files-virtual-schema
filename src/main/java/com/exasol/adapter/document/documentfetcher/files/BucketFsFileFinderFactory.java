package com.exasol.adapter.document.documentfetcher.files;

import com.exasol.adapter.document.connection.ConnectionPropertiesReader;
import com.exasol.adapter.document.files.stringfilter.*;
import com.exasol.adapter.document.files.stringfilter.wildcardexpression.WildcardExpression;

/**
 * Factory for {@link RemoteFileFinder}
 */
public class BucketFsFileFinderFactory implements FileFinderFactory {
    private static final long serialVersionUID = 3468167710047185881L;

    @Override
    public RemoteFileFinder getFinder(final StringFilter filePattern,
            final ConnectionPropertiesReader connectionPropertyReader) {
        return new BucketfsFileFinder(addPrefix(filePattern, "/bfsdefault/default/"));
    }

    private static StringFilter addPrefix(final StringFilter filePattern, final String prefix) {
        final StringFilter filePatternWithPrefix = new PrefixPrepender().prependStaticPrefix(prefix, filePattern);
        return new StringFilterFactory().and(filePatternWithPrefix, WildcardExpression.forNonWildcardPrefix(prefix));
    }

    @Override
    public String getUserGuideUrl() {
        return BucketFsVsConstants.USER_GUIDE_URL;
    }
}
