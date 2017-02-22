package com.commercetools.sunrise.common.utils;

import play.mvc.Http;
import play.utils.UriEncoding;

import java.io.UnsupportedEncodingException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static com.commercetools.sunrise.common.utils.ArrayUtils.arrayToList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public final class UrlUtils {

    private UrlUtils() {
    }

    /**
     * Builds an url encoded URI from the given path and the query string map.
     *
     * @param path URI path without query string
     * @param queryString the query string map - the values will be url encoded
     *
     * @return a URI composed by the path and query string
     */
    public static String buildUrl(final String path, final Map<String, List<String>> queryString) {
        return buildUrl(path, buildQueryString(queryString));
    }

    /**
     * Builds an url encoded URI from the given path and the query string, of the form: {@code path?foo=1&bar=2}.
     *
     * @param path the path
     * @param queryString the url encoded query string
     *
     * @return a URI composed by the path and query string
     */
    public static String buildUrl(final String path, final String queryString) {
        return path + (queryString.isEmpty() ? "" : "?" + queryString);
    }

    /**
     * Builds an url encoded query for the given query string map.
     *
     * @param queryString  the query string map - the values will be url encoded
     *
     * @return a query string with url encoded values
     */
    public static String buildQueryString(final Map<String, List<String>> queryString) {
        return queryString.entrySet().stream()
                .map(parameter -> buildQueryStringOfParameter(parameter.getKey(), parameter.getValue()))
                .filter(keyValue -> !keyValue.isEmpty())
                .collect(joining("&"));
    }

    private static String buildQueryStringOfParameter(final String key, final List<String> values) {
        if (values.isEmpty()) {
            return "";
        }
        else {
            return values.stream()
                    .map(UrlUtils::encode)
                    .collect(joining("&" + key + "=", key + "=", ""));
        }
    }

    /**
     * Url encodes the given value via {@link URLEncoder#encode(String, String)}.
     *
     * @param value the value to encode
     *
     * @return the url encoded value
     */
    private static String encode(String value) {
        try {
            final String encoded = URLEncoder.encode(value, StandardCharsets.UTF_8.name());
            return encoded;
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Couldn't encode value", e);
        }
    }

    public static Map<String, List<String>> getQueryString(final Http.Request request) {
        return request.queryString().entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> arrayToList(e.getValue())));
    }
}
