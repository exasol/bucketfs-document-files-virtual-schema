# Virtual Schema for document data on BucketFS 1.0.0, released 2022-09-29

Code name: Upgrade virtual-schema-common-document-files

## Summary

In this release we upgraded version 7.1.1 of `virtual-schema-common-document-files`, which means that it now uses the new [connection parameter specification](https://github.com/exasol/connection-parameter-specification/blob/main/specification.md). This means you need to update the connection, see the [user guide](https://github.com/exasol/bucketfs-document-files-virtual-schema/blob/main/doc/user_guide/user_guide.md#creating-a-connection) for details.

In previous versions you could configure a base path for the connection, however this was always ignored. If you need this configuration, please vote for [issue #25](https://github.com/exasol/bucketfs-document-files-virtual-schema/issues/25).

We also fixed [sonatype-2022-5401](https://ossindex.sonatype.org/vulnerability/sonatype-2022-5401) in reload4j.

## Features

* #22: Updated dependencies.
* #19: Refactored to use version 7.1.1 of `virtual-schema-common-document-files`.

## Dependency Updates

### Compile Dependency Updates

* Removed `com.exasol:exasol-test-setup-abstraction-java:0.3.2`
* Updated `com.exasol:parquet-io-java:1.3.2` to `1.3.3`
* Updated `com.exasol:virtual-schema-common-document-files:4.0.0` to `7.1.1`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.13.3` to `2.13.4`
* Removed `org.mockito:mockito-core:4.6.1`
* Removed `org.slf4j:slf4j-jdk14:1.7.36`

### Runtime Dependency Updates

* Added `org.slf4j:slf4j-jdk14:2.0.3`

### Test Dependency Updates

* Added `com.exasol:exasol-test-setup-abstraction-java:0.3.2`
* Updated `com.exasol:hamcrest-resultset-matcher:1.5.1` to `1.5.2`
* Updated `com.exasol:test-db-builder-java:3.3.3` to `3.3.4`
* Updated `com.exasol:udf-debugging-java:0.6.3` to `0.6.4`
* Updated `com.exasol:virtual-schema-common-document-files:4.0.0` to `7.1.1`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.8.2` to `5.9.1`
* Updated `org.junit.jupiter:junit-jupiter-params:5.8.2` to `5.9.1`
* Added `org.mockito:mockito-core:4.8.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.1` to `1.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:2.5.0` to `2.8.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0` to `3.1.0`
