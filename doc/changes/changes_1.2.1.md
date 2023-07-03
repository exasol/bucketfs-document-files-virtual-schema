# Virtual Schema for Document Data on BucketFS 1.2.1, released 2023-07-03

Code name: Dependency Upgrade

This release fixes vulnerabilities in transitive dependencies by updating `exasol-test-setup-abstraction-java`:

* `io.netty:netty-handler:jar:4.1.86.Final` (test dependency):
    * CVE-2023-34462, severity CWE-770: Allocation of Resources Without Limits or Throttling (6.5)
* `org.xerial.snappy:snappy-java:jar:1.1.8.3` (compile dependency):
    * CVE-2023-34453, severity CWE-190: Integer Overflow or Wraparound (7.5)
    * CVE-2023-34454, severity CWE-190: Integer Overflow or Wraparound (7.5)
    * CVE-2023-34455, severity CWE-770: Allocation of Resources Without Limits or Throttling (7.5)

## Summary

## Features

* #35: Updated dependencies
## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:7.3.0` to `7.3.3`

### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:2.0.1` to `2.0.2`
* Updated `com.exasol:virtual-schema-common-document-files:7.3.0` to `7.3.3`
* Updated `org.mockito:mockito-core:5.3.1` to `5.4.0`
