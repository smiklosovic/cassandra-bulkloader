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
