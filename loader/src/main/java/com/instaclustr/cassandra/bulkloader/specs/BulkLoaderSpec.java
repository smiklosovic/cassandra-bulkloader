package com.instaclustr.cassandra.bulkloader.specs;

import com.google.common.base.MoreObjects;
import com.instaclustr.picocli.IPartitionerConverter;
import org.apache.cassandra.dht.IPartitioner;
import picocli.CommandLine.Option;

public class BulkLoaderSpec {

    @Option(names = {"--output-dir", "-d"},
            paramLabel = "[DIRECTORY]",
            required = true,
            description = "Destination where SSTables will be generated.")
    public String outputDir;

    @Option(names = {"--keyspace", "-k"},
            paramLabel = "[KEYSPACE]",
            required = true,
            description = "Keyspace for which SSTables will be generated.")
    public String keyspace;

    @Option(names = {"--table", "-t"},
            paramLabel = "[TABLE]",
            required = true,
            description = "Table for which SSTables will be generated.")
    public String table;

    @Option(names = {"--schema", "-s"},
            paramLabel = "[PATH]",
            required = true,
            description = "Path to CQL schema where CREATE TABLE statement is specified.")
    public String schema;

    @Option(names = {"--sorted"},
            paramLabel = "[PATH]",
            required = false,
            defaultValue = "false",
            description = "Whether input data are already sorted (in terms of CQL)")
    public boolean sorted = false;

    @Option(names = {"--partitioner"},
            required = false,
            defaultValue = "murmur",
            converter = IPartitionerConverter.class,
            description = "Paritioner used for SSTable generation, defaults to 'murmur'")
    public IPartitioner partitioner;

    @Option(names = {"--bufferSize"},
            required = false,
            description = "How much data will be buffered before being written as a new SSTable, in megabytes. Defaults to 128",
            defaultValue = "128")
    public int bufferSize;

    @Option(names = {"--numberOfRecords"},
            required = false,
            defaultValue = "100",
            description = "Number of records to generate when using random command")
    public int numberOfRecords;

    @Option(names = {"--threads"},
            required = false,
            defaultValue = "1",
            description = "Number of threads to use for generation.")
    public int threads;

    @Option(names = {"--file", "-f"},
            description = "file to digest, irrelevant for random loader")
    public String file;

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("outputDir", outputDir)
                          .add("keyspace", keyspace)
                          .add("table", table)
                          .add("schema", schema)
                          .add("sorted", sorted)
                          .add("partitioner", partitioner == null ? "not set" : partitioner.getClass().getCanonicalName())
                          .add("bufferSize", bufferSize)
                          .add("numberOfRecords", numberOfRecords)
                          .add("threads", threads)
                          .add("file", file)
                          .toString();
    }
}
