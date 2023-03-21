# Virtual Schema for document data on BucketFS 0.5.1, released 2022-06-27

Code name: Update dependencies

## Summary

This release updates dependencies to fix vulnerabilities:

* com.squareup.okhttp:okhttp:jar:2.7.5:compile
  * CVE-2021-0341
  * sonatype-2018-0035
* com.fasterxml.jackson.core:jackson-databind:jar:2.13.0:compile
  * CVE-2020-36518
  * sonatype-2021-4682
* io.netty:netty-common:jar:4.1.72.Final:runtime
  * CVE-2022-24823
* org.apache.hadoop:hadoop-common:jar:3.3.1:compile
  * CVE-2022-26612
* com.google.guava:guava:jar:31.0.1-jre:compile
  * sonatype-2020-0926
* io.netty:netty-handler:jar:4.1.72.Final:runtime;
  * sonatype-2020-0026
* org.apache.xmlrpc:xmlrpc-common:jar:3.1.3:compile
  * CVE-2016-5003
  * CVE-2016-5002
* com.google.protobuf:protobuf-java:jar:2.5.0:compile
  * CVE-2021-22569
* commons-codec:commons-codec:jar:1.11:compile
  * sonatype-2012-0050
* org.apache.xmlrpc:xmlrpc-client:jar:3.1.3:compile
  * CVE-2016-5004
* com.google.code.gson:gson:jar:2.2.4:compile
  * sonatype-2021-1694

## Bugfixes

* #16: Updated dependencies

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:0.3.1` to `0.3.2`
* Added `com.exasol:parquet-io-java:1.3.1`
* Added `com.fasterxml.jackson.core:jackson-databind:2.13.3`
* Updated `org.mockito:mockito-core:4.5.1` to `4.6.1`

### Test Dependency Updates

* Updated `com.exasol:test-db-builder-java:3.3.2` to `3.3.3`
* Updated `com.exasol:udf-debugging-java:0.6.0` to `0.6.3`
* Removed `org.jacoco:org.jacoco.core:0.8.8`

### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.1` to `0.4.0`
* Updated `com.exasol:project-keeper-maven-plugin:2.3.2` to `2.4.6`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M3` to `3.0.0-M5`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M3` to `3.0.0-M5`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.9.0` to `2.10.0`
