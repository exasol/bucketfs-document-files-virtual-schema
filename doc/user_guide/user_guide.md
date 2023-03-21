# User Guide

This user guide helps you with getting started with the BucketFS Files Virtual Schemas.

## Installation

Upload the latest available [release of this adapter](https://github.com/exasol/bucketfs-document-files-virtual-schema/releases) to BucketFS. See [Create a bucket in BucketFS](https://docs.exasol.com/administration/on-premise/bucketfs/create_new_bucket_in_bucketfs_service.htm) and [Upload the driver to BucketFS](https://docs.exasol.com/administration/on-premise/bucketfs/accessfiles.htm) for details.

Then create a schema to hold the adapter script.

```sql
CREATE SCHEMA ADAPTER;
```

Next create the Adapter Script:

```sql
CREATE OR REPLACE JAVA ADAPTER SCRIPT ADAPTER.BUCKET_FS_FILES_ADAPTER AS
    %scriptclass com.exasol.adapter.RequestDispatcher;
    %jar /buckets/bfsdefault/default/document-files-virtual-schema-dist-7.2.0-bucketfs-1.1.0.jar;
/
```

In addition to the adapter script you need to create a UDF function that will handle the loading of the data:

```sql
CREATE OR REPLACE JAVA SET SCRIPT ADAPTER.IMPORT_FROM_BUCKETFS_DOCUMENT_FILES(
  DATA_LOADER VARCHAR(2000000),
  SCHEMA_MAPPING_REQUEST VARCHAR(2000000),
  CONNECTION_NAME VARCHAR(500))
  EMITS(...) AS
    %scriptclass com.exasol.adapter.document.UdfEntryPoint;
    %jar /buckets/bfsdefault/default/document-files-virtual-schema-dist-7.2.0-bucketfs-1.1.0.jar;
/
```

## Creating a Connection

Now you need to define a connection that includes the location of stored files:

```sql
CREATE CONNECTION BUCKETFS_CONNECTION
    TO ''
    USER ''
    IDENTIFIED BY '{}';
```

You must leave `TO` and `USER` empty and set `IDENTIFIED BY` to an empty JSON object `{}`.

The virtual schema will import files only from the default bucket `/buckets/bfsdefault`. In previous versions you could configure a base path for the connection. This option was removed in version 1.0.0. If you need this configuration, please vote for [issue #25](https://github.com/exasol/bucketfs-document-files-virtual-schema/issues/25).

## Defining the Schema Mapping

Before creating a Virtual Schema you need to create a mapping definition that defines how the document data is mapped to Exasol tables.

For that we use the Exasol Document Mapping Language (EDML). It is universal over all document Virtual Schemas. To learn how to define such EDML definitions check the [user guide in the common repository for all document Virtual Schemas](https://github.com/exasol/virtual-schema-common-document/blob/main/doc/user_guide/edml_user_guide.md).

In the definitions you have to define the `source` property. There you define the path to the files you can load relative to the path you defined in the connection.

This Virtual Schema adapter automatically detects the type of the document file by the file extension. You can find a list of supported file types and their extensions in the [user guide of the common repository for all file Virtual Schemas](https://github.com/exasol/virtual-schema-common-document-files/blob/main/doc/user_guide/user_guide.md).

### Example

You want to define a mapping for the file `test.json` that you uploaded to the default BucketFS (`bfsdefault`) to the default bucket (`default`). Other locations are not supported currently, see [issue #25](https://github.com/exasol/bucketfs-document-files-virtual-schema/issues/25).

Now you create a mapping definition definition with `source` set to `test.json`.

### Mapping Multiple Files

For some file type (for example JSON) each source file contains only a single document. That means, that you have one file for each row in the mapped table. To define mappings for such types, you can use the GLOB syntax. For details see the [user guide of the common repository for all file Virtual Schemas](https://github.com/exasol/virtual-schema-common-document-files/blob/main/doc/user_guide/user_guide.md#mapping-multiple-files)

## Creating the Virtual Schema

Finally create the Virtual Schema using:

```
CREATE VIRTUAL SCHEMA FILES_VS_TEST USING ADAPTER.BUCKET_FS_FILES_ADAPTER WITH
    CONNECTION_NAME = 'BUCKETFS_CONNECTION'
    MAPPING         = '/bfsdefault/default/path/to/mappings/in/bucketfs';
```

The `CREATE VIRTUAL SCHEMA` command accepts the following properties:

| Property          | Mandatory   |  Default      |   Description                                                                   |
|-------------------|-------------|---------------|---------------------------------------------------------------------------------|
|`MAPPING`          | Yes         |               | Path to the mapping definition file(s)                                          |
|`MAX_PARALLEL_UDFS`| No          | -1            | Maximum number of UDFs that are executed in parallel. -1 represents unlimited. *| 

\* The adapter will start at most one UDF per input file. That means, if data from a single file (for example a JSON-Lines file) is loaded, it will not parallelize.

Now browse the data using your favorite SQL client.
