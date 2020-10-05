# Virtual Schema for Document Files in BucketFS

[![Build Status](https://travis-ci.com/exasol/bucketfs-document-files-virtual-schema.svg?branch=master)](https://travis-ci.com/exasol/bucketfs-document-files-virtual-schema)

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Abucketfs-document-files-virtual-schema&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.exasol%3Abucketfs-document-files-virtual-schema)

[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Abucketfs-document-files-virtual-schema&metric=security_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Abucketfs-document-files-virtual-schema)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Abucketfs-document-files-virtual-schema&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Abucketfs-document-files-virtual-schema)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Abucketfs-document-files-virtual-schema&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=com.exasol%3Abucketfs-document-files-virtual-schema)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Abucketfs-document-files-virtual-schema&metric=sqale_index)](https://sonarcloud.io/dashboard?id=com.exasol%3Abucketfs-document-files-virtual-schema)

[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Abucketfs-document-files-virtual-schema&metric=code_smells)](https://sonarcloud.io/dashboard?id=com.exasol%3Abucketfs-document-files-virtual-schema)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Abucketfs-document-files-virtual-schema&metric=coverage)](https://sonarcloud.io/dashboard?id=com.exasol%3Abucketfs-document-files-virtual-schema)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Abucketfs-document-files-virtual-schema&metric=duplicated_lines_density)](https://sonarcloud.io/dashboard?id=com.exasol%3Abucketfs-document-files-virtual-schema)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=com.exasol%3Abucketfs-document-files-virtual-schema&metric=ncloc)](https://sonarcloud.io/dashboard?id=com.exasol%3Abucketfs-document-files-virtual-schema)


This Virtual Schemas allows you to access document files stored in BucketFS like any regular Exasol table.
For different file systems sources check the [files Virtual Schema][files-vs]

**Important:** BucketFS synchronizes all files over all Exasol nodes.
For that reason it is not an appropriate choice for storing large files.
This Virtual Schema exists mainly for testing and training reasons. 
For production choose a different data source.

For supported document file formats: see [files Virtual Schema][files-vs]

* [User Guide](doc/user_guide/user_guide.md)
* [Changelog](doc/changes/changelog.md)
* [Dependencies](NOTICE)

[files-vs]: https://github.com/exasol/virtual-schema-common-document-files
