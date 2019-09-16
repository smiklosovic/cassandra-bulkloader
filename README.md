# cassandra-bulkloader
CLI tool generating Cassandra SSTables

This tool is simply generating SSTables programmatically. It uses Cassandra's `CQLSSTableWriter`. 
After generation of SSTables is finished, you can load them by `sstableloader` tool as usually.

The project consists of three modules:

* api - impl is coded against this module
* impl - implementation of your population logic, depends on `api`
* loader - implementation of whole loader CLI application, depends on `impl` and `api`.

## Build 

`mvn clean instal`

## Run

```
java \
  -cp /path/to/impl-1.0.jar:/path/to/loader-1.0.jar \
  com.instaclustr.cassandra.bulkloader.CLIApplication \
  _command_ \
  _arguments_
```

No `command` executes default command - `help`:

```
Usage: <main class> [-V] COMMAND
  -V, --version   print version information and exit
Commands:
  csv     tool for bulk-loading of data from csv
  random  tool for bulk-loading of random data
```

### `random` command
```
tool for bulk-loading of random data
  -d, --output-dir=[DIRECTORY]
                            Destination where SSTables will be generated.
  -k, --keyspace=[KEYSPACE] Keyspace for which SSTables will be generated.
  -t, --table=[TABLE]       Table for which SSTables will be generated.
  -s, --schema=[PATH]       Path to CQL schema where CREATE TABLE statement is
                              specified.
      --sorted              Whether input data are already sorted (in terms of
                              CQL)
      --partitioner=<partitioner>
                            Paritioner used for SSTable generation, defaults to
                              'murmur'
      --bufferSize=<bufferSize>
                            How much data will be buffered before being written as
                              a new SSTable, in megabytes. Defaults to 128
      --numberOfRecords=<numberOfRecords>
                            Number of records to generate when using random
                              command
      --threads=<threads>   Number of threads to use for generation.
  -f, --file=<file>         file to digest, irrelevant for random loader
  -h, --help                Show this help message and exit.
  -V, --version             Print version information and exit.
```

### `csv` command

`csv` command has same arguments as `random` but `--file` is mandatory. There is supposed to be CSV file which 
is representing rows. Each row will be parsed into list of strings passed to `RowMapper` implementation where you 
have to map them to list of objects for Cassandra INSERT statement as values.

## Row generation

In order to generate data, in case of `random` generator, you have to implement interface 
`com.instaclustr.cassandra.bulkloader.RowMapper` in `api` module. This implementation should 
be placed in `impl` module.

## RowMapper interface

```
package com.instaclustr.cassandra.bulkloader;

import java.util.List;

public interface RowMapper {

    /**
     * Maps list of strings from whatever input representing
     * a row to list of objects to insert into Cassandra.
     *
     * @param row where values are consisting of list of strings
     * @return list of objects to put to insert statement
     */
    List<Object> map(final List<String> row);

    /**
     * Logically same as {@link #map(List)} but all data per row
     * needs to be generated inside of the method. The number
     * of items in the returned list has to match number of columns
     * in a row. Each such object represents value which will be
     * passed to Cassandra INSERT statement.
     *
     * This method is called repeatedly. Number of calls
     * is equal to paramter `--numberOfRecords`.
     *
     * @return list of objects to put to insert statement
     */
    List<Object> random();

    /**
     * @return string representation of INSERT INTO statement. Question marks in VALUES are not
     * meant to be replaced.
     * <p>
     * For example: 'INSERT INTO keyspace.table ("field1, "field2", ...) VALUES (?, ?, ?)'
     */
    String insertStatement();
}

```

## SPI mechanism

There is Java SPI mechanism for implementation discovery so it means that besides implementing API,
you have to change `impl/src/main/resources/META-INF/services/com.instaclustr.cassandra.bulkloader.RowMapper` 
file containing FQCN of your implemenation on one line.

Once impl jar is placed on the class path, it will be automatically discovered by `loader` module so 
you do not need to use any command-line arguments. Mere putting of that JAR on the class path does the job.

This in practice means that you need to compile only `impl` module which contains one class so the compilation 
and JAR building will take literally few seconds (less the 1 sec here). The command line arguments and all will look 
just same.
