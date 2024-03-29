# Virtual Schema for Document Data on BucketFS 1.1.0, released 2023-03-21

Code name: Auto-inference for Parquet

## Summary

This release adds automatic schema inference for Parquet files. This means that you don't need to specify a `mapping` element in the EDML definition. Instead VSBFS will automatically detect the mapping from the Parquet files. See the [EDML user guide](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/user_guide/edml_user_guide.md#automatic-mapping-inference) for details.

## Features

* #31: Added auto-inference for Parquet files

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:7.1.4` to `7.2.0`

### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.6` to `2.0.7`

### Test Dependency Updates

* Added `com.exasol:exasol-testcontainers:6.5.1`
* Updated `com.exasol:udf-debugging-java:0.6.7` to `0.6.8`
* Updated `com.exasol:virtual-schema-common-document-files:7.1.4` to `7.2.0`
* Updated `org.mockito:mockito-core:5.0.0` to `5.2.0`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:1.2.1` to `1.2.2`
* Updated `com.exasol:project-keeper-maven-plugin:2.9.1` to `2.9.5`
* Updated `org.apache.maven.plugins:maven-assembly-plugin:3.3.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.3.0` to `3.5.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.1.0` to `3.2.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M7` to `3.0.0-M8`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.13.0` to `2.14.2`
