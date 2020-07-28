package com.spectsys.banyan.utils;

import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.experimental.UtilityClass;

import java.io.IOException;

@UtilityClass
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER;
    private static final ObjectWriter OBJECT_LINE_WRITER;
    private static final ObjectWriter OBJECT_PRETTY_WRITER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        OBJECT_LINE_WRITER = OBJECT_MAPPER.writer();

        // configure pretty-printing:
        OBJECT_MAPPER.enable(SerializationFeature.INDENT_OUTPUT);
        final DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        final DefaultPrettyPrinter.Indenter defaultIndenter = new DefaultIndenter("    ", DefaultIndenter.SYS_LF);
        prettyPrinter.indentObjectsWith(defaultIndenter);
        prettyPrinter.indentArraysWith(defaultIndenter);

        OBJECT_PRETTY_WRITER = OBJECT_MAPPER.writer(prettyPrinter);
    }

    public static <T> T fromJson(
        final String jsonString,
        final Class<T> someClass
    ) {
        try {
            return OBJECT_MAPPER.readValue(jsonString, someClass);
        } catch (final IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toLineJson(
        final Object object
    ) {
        try {
            return OBJECT_LINE_WRITER.writeValueAsString(object);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String toPrettyJson(
        final Object object
    ) {
        try {
            return OBJECT_PRETTY_WRITER.writeValueAsString(object);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
