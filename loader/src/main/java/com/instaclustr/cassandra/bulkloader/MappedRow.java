package com.instaclustr.cassandra.bulkloader;

import static java.util.stream.Collectors.toList;

import java.util.Collections;
import java.util.List;

public class MappedRow {
    public final List<Object> values;
    public final List<String> types;

    public MappedRow(final List<Object> values) {
        this.values = values;
        this.types = cellTypes(values);
    }

    public List<String> cellTypes(final List<Object> row) {
        if (row == null) {
            return Collections.emptyList();
        }
        return row.stream().map(cell -> {
            if (cell != null) {
                return cell.getClass().toString();
            } else {
                return "unknown";
            }
        }).collect(toList());
    }
}
