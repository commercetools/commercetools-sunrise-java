package com.commercetools.sunrise.ctp.graphql;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class GraphQLUtils {

    private GraphQLUtils() {
    }

    public static String readFromResource(final String resourcePath) {
        final InputStream resourceAsStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourcePath);
        try {
            return IOUtils.toString(resourceAsStream, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new GraphQLException("Could not read GraphQL file", e);
        }
    }
}
