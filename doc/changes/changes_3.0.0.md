# Virtual Schema for Document Data on BucketFS 3.0.0, released 2026-10-29

Code name: Fix vulnerabilities

## Summary

This release upgrades `virtual-schema-common-document-files` to version 10.0.1, fixing vulnerabilities in CSV processing, Snappy compression, and ASM bytecode handling. Java 17 is now required to build and run this Virtual Schema.

Exasol 8 is no longer supported.

## Security

* #74
* #75
* #76
* #77
* #78
* #80
* #82
* #83
* #85
* #87
* #88
* #90
* #91
* #92
* #93
* #94
* #95
* #96
* #97

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
