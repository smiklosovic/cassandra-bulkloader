package com.instaclustr.cassandra.bulkloader.loader;

import java.util.Iterator;

import com.instaclustr.cassandra.bulkloader.RowMapper;
import com.instaclustr.cassandra.bulkloader.SSTableWriter;
import com.instaclustr.cassandra.bulkloader.SSTableGeneratorException;
import com.instaclustr.cassandra.bulkloader.MappedRow;

public class RandomLoader implements Loader {

    private final SSTableWriter ssTableWriter;
    private final int numberOfRecords;

    public RandomLoader(final SSTableWriter ssTableWriter,
                        int numberOfRecords) {
        this.ssTableWriter = ssTableWriter;
        this.numberOfRecords = numberOfRecords;
    }

    @Override
    public void load(final RowMapper rowMapper) {
        try {
            ssTableWriter.generate(new RandomIterator(rowMapper, numberOfRecords));
        } catch (final Exception ex) {
            throw new SSTableGeneratorException("Unable to generate SSTables from RandomIterator.", ex);
        }
    }

    static class RandomIterator implements Iterator<MappedRow> {

        private int currentRecords = 0;
        private final int numberOfRecords;
        private final RowMapper rowMapper;

        public RandomIterator(final RowMapper rowMapper,
                              final int numberOfRecords) {
            this.numberOfRecords = numberOfRecords;
            this.rowMapper = rowMapper;
        }

        @Override
        public boolean hasNext() {
            return currentRecords++ < numberOfRecords;
        }

        @Override
        public MappedRow next() {

            if (currentRecords % 10000 == 0) {
                System.out.println(currentRecords);
            }

            return new MappedRow(rowMapper.random());
        }
    }
}
