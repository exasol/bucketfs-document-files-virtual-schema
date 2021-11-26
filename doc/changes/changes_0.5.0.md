# Virtual Schema for document data on BucketFS 0.5.0, released 2021-11-26

Code name: Improved Parquet Loading Performance

## Summary

In this release, we updated the base of this implementation (virtual-schema-common-document-files). By that, we improved the performance for loading parquet files. The improvements will give you a speed-up when you load a few (<200) files.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:0.2.0` to `0.2.1`
* Updated `com.exasol:virtual-schema-common-document-files:2.2.0` to `4.0.0-SNAPSHOT`
* Updated `org.mockito:mockito-core:3.12.4` to `4.1.0`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.5.0` to `1.5.1`
* Updated `com.exasol:udf-debugging-java:0.4.0` to `0.4.1`
* Updated `com.exasol:virtual-schema-common-document-files:2.2.0` to `4.0.0-SNAPSHOT`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:0.1.1` to `0.6.0`
* Updated `com.exasol:project-keeper-maven-plugin:1.2.0` to `1.3.4`
