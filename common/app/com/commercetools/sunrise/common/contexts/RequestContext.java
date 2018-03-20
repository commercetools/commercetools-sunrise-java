package com.commercetools.sunrise.common.contexts;

import java.util.List;
import java.util.Map;

public interface RequestContext {

    /**
     * Path of the current request, excluding the query string.
     * @return the path without the query string
     */
    String getPath();

    /**
     * Map for every value of the query string.
     * Each entry of the form [key - [foo, bar]] corresponds to the query string {@code key=foo&key=bar}.
     * @return the key-values of the query string
     */
    Map<String, List<String>> getQueryString();

    /**
     * Returns the URI built with the path and query string, together with the given {@code key} and {@code values}.
     * @param key query string key
     * @param values query string values for the given key
     * @return the built URI
     */
    String buildUrl(final String key, final List<String> values);

    static RequestContext of(final Map<String, List<String>> queryString, final String path) {
        return new RequestContextImpl(queryString, path);
    }
}
