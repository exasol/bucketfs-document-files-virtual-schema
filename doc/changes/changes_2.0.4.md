# Virtual Schema for Document Data on BucketFS 2.0.4, released 2024-11-19

Code name: Fix CVE-2024-25638, CVE-2024-47561 & CVE-2024-47535

## Summary

This release fixes vulnerabilties
* CVE-2024-25638 in `dnsjava:dnsjava:jar:3.4.0:compile`
* CVE-2024-47535 in `io.netty:netty-common:jar:4.1.108.Final:compile`
* CVE-2024-47561 in `org.apache.avro:avro:jar:1.11.3:compile`

## Security

* #56: Fixed vulnerability CVE-2024-25638 in `dnsjava:dnsjava:jar:3.4.0:compile`
* #58: Fixed vulnerabilitiy CVE-2024-47561 in `org.apache.avro:avro:jar:1.11.3:compile`
* #59: Fixed vulnerabilitiy CVE-2024-47535 in `io.netty:netty-common:jar:4.1.108.Final:compile`

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:8.0.4` to `8.1.5`

### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.12` to `2.0.16`

### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:2.1.2` to `2.1.6`
* Updated `com.exasol:hamcrest-resultset-matcher:1.6.5` to `1.7.0`
* Updated `com.exasol:test-db-builder-java:3.5.4` to `3.6.0`
* Updated `com.exasol:udf-debugging-java:0.6.12` to `0.6.13`
* Updated `com.exasol:virtual-schema-common-document-files:8.0.4` to `8.1.5`
* Updated `commons-net:commons-net:3.10.0` to `3.11.1`
* Updated `org.hamcrest:hamcrest:2.2` to `3.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.10.2` to `5.11.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.10.2` to `5.11.3`
* Updated `org.mockito:mockito-core:5.11.0` to `5.14.2`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.3.2` to `4.4.0`
* Added `com.exasol:quality-summarizer-maven-plugin:0.2.0`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.16` to `0.17`
* Updated `org.apache.maven.plugins:maven-clean-plugin:2.5` to `3.4.0`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.6.1` to `3.8.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.2.5` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-install-plugin:2.4` to `3.1.3`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.4.1` to `3.4.2`
* Updated `org.apache.maven.plugins:maven-resources-plugin:2.6` to `3.3.1`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.3` to `3.9.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.2.5` to `3.5.1`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.16.2` to `2.17.1`
