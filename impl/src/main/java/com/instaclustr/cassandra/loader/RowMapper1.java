package com.instaclustr.cassandra.loader;

import java.util.ArrayList;
import java.util.List;

import com.instaclustr.cassandra.bulkloader.RowMapper;

public class RowMapper1 implements RowMapper {

    @Override
    public List<Object> map(final List<String> row) {
        return new ArrayList<Object>() {{
            add(null); // text
            //add(null); // timestamp
            //add(null); // uuid
        }};
    }

    @Override
    public List<Object> random() {
        return new ArrayList<Object>() {{
            add(new Object());
            //add(new Object());
            //add(new Object());
        }};
    }

    @Override
    public String insertStatement() {
        // return string 'INSERT INTO keyspace.table ("field1, "field2", ...) VALUES (?, ?, ?);'
        return null;
    }
}
