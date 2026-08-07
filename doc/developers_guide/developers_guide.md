# Developers Guide

This guide contains information for developers.

## Running Regression Test

This project contains some regression tests to monitor the performance cross releases. To run them locally use:

```shell
mvn verify -P regressionTests
```

However, a local run won't give you reliable numbers, since it's dependent on your local hardware configuration.

## Enable Debug Log From `IMPORT_FROM_BUCKETFS_DOCUMENT_FILES`

To configure logging for UDF `IMPORT_FROM_BUCKETFS_DOCUMENT_FILES`, temporarily modify `UdfEntryPoint.run()` to include the following:

```java
final RemoteLogManager remoteLogManager = new RemoteLogManager();
remoteLogManager.setupConsoleLogger(Level.ALL);
```

This is a workaround until [virtual-schema-common-document#218](https://github.com/exasol/virtual-schema-common-document/issues/218) is implemented.
