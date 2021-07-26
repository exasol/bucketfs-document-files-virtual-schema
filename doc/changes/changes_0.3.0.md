# bucketfs-document-files-virtual-schema 0.3.0, released 2021-07-26

Code name: Parquet support

## Summary

In this release we added support for parquet files. In addition, we did some performance optimizations.

## Dependency Updates

### Compile Dependency Updates

* Added `com.exasol:exasol-test-setup-abstraction-java:0.1.1`
* Updated `com.exasol:virtual-schema-common-document-files:1.0.0` to `2.0.0`

### Runtime Dependency Updates

* Added `org.jacoco:org.jacoco.agent:0.8.5`

### Test Dependency Updates

* Removed `com.exasol:exasol-testcontainers:3.3.1`
* Updated `com.exasol:udf-debugging-java:0.3.0` to `0.4.0`
* Updated `com.exasol:virtual-schema-common-document-files:1.0.0` to `2.0.0`
* Removed `org.jacoco:org.jacoco.agent:0.8.5`
* Removed `org.testcontainers:junit-jupiter:1.15.0`

### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.3.1` to `0.4.0`
* Added `com.exasol:error-code-crawler-maven-plugin:0.1.1`
* Updated `com.exasol:project-keeper-maven-plugin:0.3.0` to `0.10.0`
* Added `io.github.zlika:reproducible-build-maven-plugin:0.13`
* Removed `org.apache.maven.plugins:maven-assembly-plugin:3.3.0`
* Added `org.apache.maven.plugins:maven-shade-plugin:3.2.4`
