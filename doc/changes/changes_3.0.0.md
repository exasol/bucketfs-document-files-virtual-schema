# Virtual Schema for Document Data on BucketFS 3.0.0, released 2026-10-29

Code name: Fix vulnerabilities

## Summary

This release upgrades `virtual-schema-common-document-files` to version 10.0.1, fixing vulnerabilities in CSV processing, Snappy compression, and ASM bytecode handling. Java 17 is now required to build and run this Virtual Schema.

Exasol 8 is no longer supported.

## Security

* #74: Fixed CVE-2026-73334 (untrusted KMS URL) in `org.apache.parquet:parquet-hadoop:1.17.1`.
* #75: Fixed CVE-2026-86231 (certificate-revocation bypass) in `com.github.mwiede:jsch:2.28.4`.
* #76: Fixed CVE-2026-87795 (dictionary bounds read) in `com.github.luben:zstd-jni:1.5.7-3`.
* #77: Fixed CVE-2026-87823 (signed bounds overflow) in `com.github.luben:zstd-jni:1.5.7-3`.
* #78: Fixed CVE-2026-89045 (negative-length infinite loop) in `com.github.luben:zstd-jni:1.5.7-3`.
* #80: Fixed CVE-2026-90559 (ByteBuffer out-of-bounds write) in `org.xerial.snappy:snappy-java:1.1.10.7`.
* #82: Fixed CVE-2026-93451 (typed-array buffer overflow) in `org.xerial.snappy:snappy-java:1.1.10.7`.
* #83: Fixed CVE-2026-93452 (ByteBuffer buffer overflow) in `org.xerial.snappy:snappy-java:1.1.10.7`.
* #85: Fixed CVE-2026-104880 (CSV interpretation conflict) in `de.siegmar:fastcsv:3.7.0`.
* #87: Fixed CVE-2026-108104 (pooled-buffer double release) in `org.xerial.snappy:snappy-java:1.1.10.7`.
* #88: Fixed CVE-2026-108106 (unbounded decompression allocation) in `org.xerial.snappy:snappy-java:1.1.10.7`.
* #90: Fixed CVE-2026-110262 (AdviceAdapter memory exhaustion) in `org.ow2.asm:asm-commons:9.10.1`.
* #91: Fixed CVE-2026-110270 (JSR inlining memory exhaustion) in `org.ow2.asm:asm-commons:9.10.1`.
* #92: Fixed CVE-2026-110257 (bootstrap-method allocation) in `org.ow2.asm:asm:9.10.1`.
* #93: Fixed CVE-2026-110258 (invalid RET allocation) in `org.ow2.asm:asm:9.10.1`.
* #94: Fixed CVE-2026-110259 (recursive instruction rewriting) in `org.ow2.asm:asm:9.10.1`.
* #95: Fixed CVE-2026-110260 (exception-handler memory exhaustion) in `org.ow2.asm:asm:9.10.1`.
* #96: Fixed CVE-2026-110261 (frame-computation CPU exhaustion) in `org.ow2.asm:asm:9.10.1`.
* #97: Fixed CVE-2026-110263 (nested-signature stack exhaustion) in `org.ow2.asm:asm:9.10.1`.

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:9.1.0` to `10.0.1`

### Test Dependency Updates

* Updated `com.exasol:exasol-test-setup-abstraction-java:3.0.0` to `3.0.1`
* Updated `com.exasol:udf-debugging-java:0.6.20` to `0.6.22`
* Updated `com.exasol:virtual-schema-common-document-files:9.1.0` to `10.0.1`
* Updated `org.junit.jupiter:junit-jupiter-api:5.14.4` to `6.1.3`
* Updated `org.junit.jupiter:junit-jupiter-params:5.14.4` to `6.1.3`
* Added `org.objenesis:objenesis:3.6`

### Plugin Dependency Updates

* Updated `com.exasol:error-code-crawler-maven-plugin:2.1.0` to `2.1.2`
* Updated `com.exasol:project-keeper-maven-plugin:5.7.4` to `5.8.1`
* Updated `io.github.git-commit-id:git-commit-id-maven-plugin:10.0.0` to `10.0.1`
* Updated `org.apache.maven.plugins:maven-artifact-plugin:3.6.1` to `3.7.0`
* Updated `org.apache.maven.plugins:maven-compiler-plugin:3.15.0` to `3.16.0`
* Updated `org.apache.maven.plugins:maven-failsafe-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-install-plugin:3.1.4` to `3.2.0`
* Updated `org.apache.maven.plugins:maven-jar-plugin:3.5.0` to `3.5.1`
* Updated `org.apache.maven.plugins:maven-surefire-plugin:3.5.6` to `3.6.0`
* Updated `org.apache.maven.plugins:maven-toolchains-plugin:3.2.0` to `3.3.0`
* Updated `org.codehaus.mojo:flatten-maven-plugin:1.7.3` to `1.8.0`
* Updated `org.codehaus.mojo:versions-maven-plugin:2.21.0` to `2.23.0`
* Updated `org.sonarsource.scanner.maven:sonar-maven-plugin:5.7.0.6970` to `5.8.0.7211`
