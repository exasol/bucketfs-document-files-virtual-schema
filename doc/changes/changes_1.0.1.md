# Virtual Schema for document data on BucketFS 1.0.1, released 2022-??-??

Code name: Dependency update

## Summary

Updated dependencies to fix vulnerabilities.

## Bug Fixes

* #26: Fixed vulnerabilities.
  * [org.apache.hadoop:hadoop-common:jar:3.3.4](https://ossindex.sonatype.org/component/pkg:maven/org.apache.hadoop/hadoop-common@3.3.4?utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1) in compile
    * [sonatype-2022-5820](https://ossindex.sonatype.org/vulnerability/sonatype-2022-5820): 1 vulnerability (8.2)
  * [org.apache.hadoop:hadoop-hdfs-client:jar:3.3.4](https://ossindex.sonatype.org/component/pkg:maven/org.apache.hadoop/hadoop-hdfs-client@3.3.4?utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1) in compile
    * [sonatype-2022-5732](https://ossindex.sonatype.org/vulnerability/sonatype-2022-5732): 1 vulnerability (8.6)
  * [com.fasterxml.jackson.core:jackson-databind:jar:2.13.4](https://ossindex.sonatype.org/component/pkg:maven/com.fasterxml.jackson.core/jackson-databind@2.13.4?utm_source=ossindex-client&utm_medium=integration&utm_content=1.8.1) in compile
    * CVE-2022-42003, severity CWE-502: Deserialization of Untrusted Data (7.5)

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:parquet-io-java:1.3.3` to `2.0.0`
* Updated `com.fasterxml.jackson.core:jackson-databind:2.13.4` to `2.13.4.2`

### Test Dependency Updates

* Updated `com.exasol:test-db-builder-java:3.3.4` to `3.4.1`
* Updated `org.mockito:mockito-core:4.8.0` to `4.8.1`
