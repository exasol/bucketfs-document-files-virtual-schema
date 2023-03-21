# Virtual Schema for document data on BucketFS 1.0.1, released 2023-01-23

Code name: Dependency upgrade

## Summary

Removed references to discontinued repository `maven.exasol.com`, fixed and reviewed vulnerabilities.

## Bugfixes

* #28 Fixed vulnerabilities
  * Overrode transitive test dependency [commons-net:commons-net:jar:3.6](https://ossindex.sonatype.org/component/pkg:maven/commons-net/commons-net@3.6?utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1) via `com.exasol:virtual-schema-common-document-files` to fix vulnerability `CVE-2021-37533`.
  * Ignored vulnerabilities
    * sonatype-2022-6438 in [com.fasterxml.jackson.core:jackson-core:jar:2.14.1](https://ossindex.sonatype.org/component/pkg:maven/com.fasterxml.jackson.core/jackson-core@2.14.1?utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1) in compile as an exploit requires write access to the source code repository and attempts to inject malicious code will be detected during regular code reviews.
    * [CVE-2020-36641](https://nvd.nist.gov/vuln/detail/CVE-2020-36641) in transitive test dependency to [fr.turri:aXMLRPC:jar:1.13.0](https://ossindex.sonatype.org/component/pkg:maven/fr.turri/aXMLRPC@1.13.0?utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1) via `com.exasol:exasol-test-setup-abstraction-java` as vulnerability is rated as a false positive since CVE-2020-36641 is reported to be fixed in aXMLRPC 1.12.1, while ETAJ uses version 1.13.0.

* #26: Fixed vulnerabilities.
  * [org.apache.hadoop:hadoop-common:jar:3.3.4](https://ossindex.sonatype.org/component/pkg:maven/org.apache.hadoop/hadoop-common@3.3.4?utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1) in compile
    * sonatype-2022-5820: 1 vulnerability (8.2)
  * [org.apache.hadoop:hadoop-hdfs-client:jar:3.3.4](https://ossindex.sonatype.org/component/pkg:maven/org.apache.hadoop/hadoop-hdfs-client@3.3.4?utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1) in compile
    * sonatype-2022-5732: 1 vulnerability (8.6)
  * [com.fasterxml.jackson.core:jackson-databind:jar:2.13.4](https://ossindex.sonatype.org/component/pkg:maven/com.fasterxml.jackson.core/jackson-databind@2.13.4?utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1) in compile
    * CVE-2022-42003, severity CWE-502: Deserialization of Untrusted Data (7.5)

## Dependency Updates

### Compile Dependency Updates

* Removed `com.exasol:parquet-io-java:1.3.3`
* Updated `com.exasol:virtual-schema-common-document-files:7.1.1` to `7.1.4`
* Removed `com.fasterxml.jackson.core:jackson-databind:2.13.4`

### Runtime Dependency Updates

* Updated `org.slf4j:slf4j-jdk14:2.0.3` to `2.0.6`

### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:0.3.2` to `2.0.0`
* Updated `com.exasol:test-db-builder-java:3.3.4` to `3.4.2`
* Updated `com.exasol:udf-debugging-java:0.6.4` to `0.6.7`
* Updated `com.exasol:virtual-schema-common-document-files:7.1.1` to `7.1.4`
* Added `commons-net:commons-net:3.9.0`
* Updated `org.junit.jupiter:junit-jupiter-engine:5.9.1` to `5.9.2`
* Updated `org.junit.jupiter:junit-jupiter-params:5.9.1` to `5.9.2`
* Updated `org.mockito:mockito-core:4.8.0` to `5.0.0`

### Plugin Dependency Updates

* Updated `com.exasol:artifact-reference-checker-maven-plugin:0.4.0` to `0.4.2`
* Updated `com.exasol:error-code-crawler-maven-plugin:1.1.2` to `1.2.1`
* Updated `com.exasol:project-keeper-maven-plugin:2.8.0` to `2.9.1`
* Updated `io.github.zlika:reproducible-build-maven-plugin:0.15` to `0.16`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.0.0-M5` to `3.0.0-M7`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.2.2` to `3.3.0`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.0.0-M5` to `3.0.0-M7`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.2.7` to `1.3.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.10.0` to `2.13.0`
