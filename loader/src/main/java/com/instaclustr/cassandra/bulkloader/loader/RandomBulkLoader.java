package com.instaclustr.cassandra.bulkloader.loader;

import static com.instaclustr.cassandra.bulkloader.CLIApplication.execute;

import com.instaclustr.cassandra.bulkloader.BulkLoader;
import com.instaclustr.cassandra.bulkloader.CLIApplication;
import com.instaclustr.cassandra.bulkloader.SSTableWriter;
import com.instaclustr.cassandra.bulkloader.specs.BulkLoaderSpec;
import picocli.CommandLine.Command;

@Command(name = "random",
         mixinStandardHelpOptions = true,
         description = "tool for bulk-loading of random data",
         sortOptions = false,
         versionProvider = CLIApplication.class)
public class RandomBulkLoader extends BulkLoader {

    public static void main(String[] args) {
        System.exit(execute(new CSVBulkLoader(), args));
    }

    @Override
    public Loader getLoader(final BulkLoaderSpec bulkLoaderSpec, final SSTableWriter ssTableWriter) {
        return new RandomLoader(ssTableWriter, bulkLoaderSpec.numberOfRecords);
    }
}
