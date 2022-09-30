package com.exasol.adapter.document.files;

import static com.exasol.adapter.document.files.BucketfsDocumentFilesAdapter.ADAPTER_NAME;

import com.exasol.adapter.AdapterFactory;
import com.exasol.adapter.VirtualSchemaAdapter;
import com.exasol.adapter.document.DocumentAdapter;
import com.exasol.adapter.document.documentfetcher.files.BucketFsFileFinderFactory;
import com.exasol.logging.VersionCollector;

/**
 * Factory for {@link DocumentFilesAdapter}.
 */
public class BucketfsDocumentFilesAdapterFactory implements AdapterFactory {
    @Override
    public VirtualSchemaAdapter createAdapter() {
        return new DocumentAdapter(new DocumentFilesAdapter(ADAPTER_NAME, new BucketFsFileFinderFactory()));
    }

    @Override
    public String getAdapterVersion() {
        final VersionCollector versionCollector = new VersionCollector(
                "META-INF/maven/com.exasol/bucketfs-document-files-virtual-schema/pom.properties");
        return versionCollector.getVersionNumber();
    }

    @Override
    public String getAdapterName() {
        return ADAPTER_NAME;
    }
}
