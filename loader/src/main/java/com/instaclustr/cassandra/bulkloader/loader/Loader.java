package com.instaclustr.cassandra.bulkloader.loader;

import com.instaclustr.cassandra.bulkloader.RowMapper;

public interface Loader {

    void load(final RowMapper rowMapper);
}
