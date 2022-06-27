# Virtual Schema for document data on BucketFS 0.5.0, released 2022-05-10

Code name: Improved Parquet Loading Performance

## Summary

In this release, we updated the base of this implementation (virtual-schema-common-document-files). By that, we improved the performance for loading parquet files. The improvements will give you a speed-up when you load a few (<200) files.

## Documentation

* #12: Removed SQL_DIALECT property from documentation

## Bugfixes

* #14: Upgraded dependencies to fix CVE-2022-21724 in the PostgreSQL JDBC driver.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:0.2.0` to `0.3.1`
* Updated `com.exasol:virtual-schema-common-document-files:2.2.0` to `4.0.0`
* Updated `org.mockito:mockito-core:3.12.4` to `4.5.1`
* Updated `org.slf4j:slf4j-jdk14:1.7.32` to `1.7.36`

### Test Dependency Updates

* Updated `com.exasol:hamcrest-resultset-matcher:1.5.0` to `1.5.1`
* Updated `com.exasol:test-db-builder-java:3.2.1` to `3.3.2`
* Updated `com.exasol:udf-debugging-java:0.4.0` to `0.6.0`
* Updated `com.exasol:virtual-schema-common-document-files:2.2.0` to `4.0.0`
* Updated `org.jacoco:org.jacoco.agent:0.8.7` to `0.8.8`
* Updated `org.jacoco:org.jacoco.core:0.8.7` to `0.8.8`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.8.1` to `5.8.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.8.1` to `5.8.2`

### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.0` to `0.4.1`
* Updated `com.exasol:error-code-crawler-maven-plugin:0.1.1` to `1.1.1`
* Updated `com.exasol:project-keeper-maven-plugin:1.2.0` to `2.3.2`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.13` to `0.15`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.8.1` to `3.10.1`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:2.8` to `3.3.0`
* Updated `org.apache.maven.plugins:maven-enforcer-plugin:3.0.0-M3` to `3.0.0`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.2.0` to `3.2.2`
* Added `org.codehaus.mojo:flatten-maven-plugin:1.2.7`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.7` to `2.9.0`
* Updated `org.jacoco:jacoco-maven-plugin:0.8.7` to `0.8.8`
* Added `org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.1.2184`
* Updated `org.sonatype.ossindex.maven:ossindex-maven-plugin:3.1.0` to `3.2.0`
