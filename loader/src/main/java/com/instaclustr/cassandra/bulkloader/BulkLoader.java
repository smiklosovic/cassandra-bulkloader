package com.instaclustr.cassandra.bulkloader;

import static com.instaclustr.picocli.JarManifestVersionProvider.logCommandVersionInformation;

import java.util.Arrays;
import java.util.Iterator;
import java.util.ServiceLoader;

import com.instaclustr.cassandra.bulkloader.loader.Loader;
import com.instaclustr.cassandra.bulkloader.specs.BulkLoaderSpec;
import org.apache.cassandra.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Mixin;
import picocli.CommandLine.Spec;

public abstract class BulkLoader implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(BulkLoader.class);

    @Spec
    private CommandLine.Model.CommandSpec spec;

    @Mixin
    private BulkLoaderSpec bulkLoaderSpec;

    public abstract Loader getLoader(final BulkLoaderSpec bulkLoaderSpec, final SSTableWriter ssTableWriter);

    public void run() {
        logCommandVersionInformation(spec);

        logger.info(spec.toString());

        final RowMapper rowMapper = getRowMapper();

        Config.setClientMode(true);

        final GenerationThread[] threads = new GenerationThread[bulkLoaderSpec.threads];

        for (int i = 0; i < bulkLoaderSpec.threads; i++) {
            threads[i] = new GenerationThread(getLoader(bulkLoaderSpec, new SSTableWriter(bulkLoaderSpec, rowMapper.insertStatement())), getRowMapper());
            threads[i].start();
        }

        while (!Arrays.stream(threads).allMatch(val -> val.status)) {
            try {
                Thread.sleep(60 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class GenerationThread extends Thread {
        private final Loader loader;
        private final RowMapper rowMapper;
        private boolean status = false;

        public GenerationThread(final Loader loader,
                                final RowMapper rowMapper) {
            this.loader = loader;
            this.rowMapper = rowMapper;
        }

        @Override
        public void run() {
            loader.load(rowMapper);
            status = true;
        }
    }

    private RowMapper getRowMapper() {
        final ServiceLoader<RowMapper> serviceLoader = ServiceLoader.load(RowMapper.class);

        final Iterator<RowMapper> iterator = serviceLoader.iterator();

        if (iterator.hasNext()) {
            return iterator.next();
        }

        throw new IllegalStateException("There is not any implementation of RowMapper on the class path.");
    }
}
