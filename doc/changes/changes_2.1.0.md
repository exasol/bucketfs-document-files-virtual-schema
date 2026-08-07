# Virtual Schema for Document Data on BucketFS 2.1.0, released 2026-08-04

Code name: `TIMESTAMP` Precision Support

## Summary

This release adds support for `TIMESTAMP` precision. This means you can use `TIMESTAMP(0)` to `TIMESTAMP(9)` in your EDML mapping for all supported data types CSV, JSON and Parquet.

Please note that JSON files only support timestamps for number fields, interpreting the number as milliseconds since epoch. To enable this conversion, you need to set `notTimestampBehavior=CONVERT_OR_ABORT` for that column in the EDML definition file.

## Features

* #53: Added `TIMESTAMP` precision support

## Dependency Updates

### Compile Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:9.0.2` to `9.1.0`

### Test Dependency Updates

* Updated `com.exasol:virtual-schema-common-document-files:9.0.2` to `9.1.0`
