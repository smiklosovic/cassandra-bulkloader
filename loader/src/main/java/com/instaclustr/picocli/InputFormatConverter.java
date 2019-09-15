package com.instaclustr.picocli;

import static java.util.Arrays.asList;

import com.google.common.base.MoreObjects;
import picocli.CommandLine;

public class InputFormatConverter implements CommandLine.ITypeConverter<InputFormatConverter.InputFormat> {

    @Override
    public InputFormat convert(final String value) throws Exception {

        final InputFormat inputFormat = InputFormat.parse(value);

        if (inputFormat == null) {
            throw new IllegalStateException(String.format("Unsupported input format '%s', supported are: %s", value, asList(InputFormat.values())));
        }

        return inputFormat;
    }

    public enum InputFormat {
        CSV("csv"),
        RANDOM("random");

        private final String name;

        InputFormat(final String name) {
            this.name = name;
        }

        public static InputFormat parse(final String name) {
            if (name == null) {
                return null;
            }

            for (final InputFormat ifc : InputFormat.values()) {
                if (ifc.name.equals(name.toLowerCase())) {
                    return ifc;
                }
            }

            return null;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(InputFormat.this).add("name", name).toString();
        }
    }
}
