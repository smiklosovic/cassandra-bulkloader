package com.instaclustr.picocli;

import static java.lang.String.format;
import static java.util.Arrays.asList;

import com.google.common.base.MoreObjects;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.dht.Murmur3Partitioner;
import picocli.CommandLine;

public class IPartitionerConverter implements CommandLine.ITypeConverter<IPartitioner> {

    @Override
    public IPartitioner convert(final String value) {
        if (Partitioner.parse(value) == Partitioner.MURMUR) {
            return new Murmur3Partitioner();
        }
        throw new IllegalStateException(format("Unsupported partitioner '%s', supported are: %s", value, asList(Partitioner.values())));
    }

    private enum Partitioner {
        MURMUR("murmur");

        private final String name;

        Partitioner(final String name) {
            this.name = name;
        }

        public static Partitioner parse(final String name) {
            if (name == null) {
                return null;
            }

            for (final Partitioner p : Partitioner.values()) {
                if (p.name.equals(name.toLowerCase())) {
                    return p;
                }
            }

            return null;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(Partitioner.this).add("name", name).toString();
        }
    }
}
