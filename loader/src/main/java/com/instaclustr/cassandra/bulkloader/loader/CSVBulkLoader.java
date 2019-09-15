package com.instaclustr.cassandra.bulkloader.loader;

import static com.instaclustr.cassandra.bulkloader.CLIApplication.execute;
import static java.lang.String.format;

import java.nio.file.Files;
import java.nio.file.Paths;

import com.instaclustr.cassandra.bulkloader.BulkLoader;
import com.instaclustr.cassandra.bulkloader.CLIApplication;
import com.instaclustr.cassandra.bulkloader.SSTableGeneratorException;
import com.instaclustr.cassandra.bulkloader.SSTableWriter;
import com.instaclustr.cassandra.bulkloader.specs.BulkLoaderSpec;
import picocli.CommandLine.Command;

@Command(name = "csv",
         mixinStandardHelpOptions = true,
         description = "tool for bulk-loading of data from csv",
         sortOptions = false,
         versionProvider = CLIApplication.class)
public class CSVBulkLoader extends BulkLoader {

    public static void main(String[] args) {
        System.exit(execute(new CSVBulkLoader(), args));
    }

    public Loader getLoader(final BulkLoaderSpec bulkLoaderSpec, final SSTableWriter ssTableWriter) {
        return new CSVLoader(ssTableWriter, () -> {
            try {
                if (!Files.exists(Paths.get(bulkLoaderSpec.file))) {
                    throw new IllegalStateException(format("File %s does not exist!", bulkLoaderSpec.file));
                }
                return CSVLoader.CsvListReaderFactory.fromFile(bulkLoaderSpec.file);
            } catch (Exception ex) {
                throw new SSTableGeneratorException("Unable to create CsvListReader.", ex);
            }
        });
    }
}
