package com.instaclustr.cassandra.bulkloader;

public class SSTableGeneratorException extends RuntimeException {
    public SSTableGeneratorException() {
    }

    public SSTableGeneratorException(final String message) {
        super(message);
    }

    public SSTableGeneratorException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
