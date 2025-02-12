# Virtual Schema for Document Data on BucketFS 2.0.5, released 2025-02-12

Code name: Fix vulnerabilities CVE-2025-25193 and CVE-2025-24970 in dependencies

## Summary

This release fixes the following vulnerabilities in dependencies:

* `io.netty:netty-common:jar:4.1.115.Final:compile`: CVE-2025-25193
* `io.netty:netty-handler:jar:4.1.115.Final:test`: CVE-2025-24970

## Security

* #61: Fixed CVE-2025-25193 in `io.netty:netty-common:jar:4.1.115.Final:compile`
* #62: Fixed CVE-2025-24970 in `io.netty:netty-handler:jar:4.1.115.Final:test`


## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:8.1.5` to `8.1.6`

### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:2.1.6` to `2.1.7`
* Updated `com.exasol:udf-debugging-java:0.6.13` to `0.6.14`
* Updated `com.exasol:virtual-schema-common-document-files:8.1.5` to `8.1.6`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.11.3` to `5.11.4`
* Updated `org.junit.jupiter:junit-jupiter-params:5.11.3` to `5.11.4`
* Updated `org.mockito:mockito-core:5.14.2` to `5.15.2`

### Plugin Dependency Updates

* Updated `com.exasol:project-keeper-maven-plugin:4.4.0` to `4.5.0`
* Updated `org.apache.maven.plugins:maven-dependency-plugin:3.8.0` to `3.8.1`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.1` to `3.5.2`
* Updated `org.apache.maven.plugins:maven-site-plugin:3.9.1` to `3.21.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.1` to `3.5.2`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.17.1` to `2.18.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:4.0.0.4121` to `5.0.0.4389`
