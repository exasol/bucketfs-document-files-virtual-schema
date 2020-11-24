# bucketfs-document-files-virtual-schema 0.2.0, released 2020-11-24
 
Code name: Selection on source name
 
## Summary

This release implements the changes for the virtual-schema-common-document-files 3.0.0 release.
By that this dialect now supports:
* A `SOURCE_REFERENCE` column that contains the name of the S3 URI of the document
* Selection on the `SOURCE_REFERENCE` column.

The changes also cause an API change for the UDF definition. See the [user guide](../user_guide/user_guide.md).

Known issues:

* Certain virtual-schema queries can cause a database crash. For details see [Issue 41](https://github.com/exasol/virtual-schema-common-document-files/issues/41).

## Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-file:1.0.0` (was 0.2.0)
* Added `com.exasol:test-db-builder-java:2.0.0` 
* Added `com.exasol:udf-debugging-java:0.3.0` 
* Updated to `com.exasol:project-keeper-maven-plugin:0.3.0` (was 0.1.0) 
* Updated to `org.mockito:mockito-core:3.6.0` (was 3.5.13) 
* Updated to `com.exasol:exasol-testcontainers:3.3.1` (was 3.3.0) 
* Updated to `com.exasol:hamcrest-resultset-matcher:1.2.2` (was 1.2.1)
* Removed `org.junit.platform:junit-platform-runner`
* Updated to `org.junit.jupiter:junit-jupiter-engine:5.7.0` (was 5.6.2)
* Updated to `org.junit.jupiter:junit-jupiter-params:5.7.0` (was 5.6.2)