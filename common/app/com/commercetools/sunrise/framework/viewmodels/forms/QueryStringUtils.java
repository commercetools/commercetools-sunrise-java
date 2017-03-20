package com.commercetools.sunrise.framework.viewmodels.forms;

import play.mvc.Http;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public final class QueryStringUtils {

    private QueryStringUtils() {
    }

    /**
     * Finds all selected values for this field name in the HTTP request.
     * @param fieldName name of the form field
     * @param httpRequest current HTTP request
     * @return all the selected values for this field
     */
    public static List<String> findAllSelectedValuesFromQueryString(final String fieldName, final Http.Request httpRequest) {
        final String[] values = httpRequest.queryString().getOrDefault(fieldName, new String[]{});
        return asList(values);
    }

    /**
     * Finds one selected value for this field name in the HTTP request.
     * @param fieldName name of the form field
     * @param httpRequest current HTTP request
     * @return a selected value for this field, or empty if none is selected
     */
    public static Optional<String> findSelectedValueFromQueryString(final String fieldName, final Http.Request httpRequest) {
        return Optional.ofNullable(httpRequest.getQueryString(fieldName));
    }

    /**
     * Extracts the query string from the request.
     * @param httpRequest current HTTP request
     * @return query string from the request
     */
    public static Map<String, List<String>> extractQueryString(final Http.Request httpRequest) {
        return httpRequest.queryString().entrySet().stream()
                .collect(toMap(Map.Entry::getKey, entry -> asList(entry.getValue())));
    }

    /**
     * Builds a URI from the given path and the query string, of the form: {@code path?foo=1&bar=2}
     * @param path URI path without query string
     * @param queryString the query string
     * @return a URI composed by the path and query string
     */
    public static String buildUri(final String path, final Map<String, List<String>> queryString) {
        final String queryStringAsString = buildQueryString(queryString);
        final String connector;
        if (queryStringAsString.isEmpty()) {
            connector = "";
        } else if (path.contains("?")) {
            connector = "&";
        } else {
            connector = "?";
        }
        return path + connector + queryStringAsString;
    }

    private static String buildQueryString(final Map<String, List<String>> queryString) {
        return queryString.entrySet().stream()
                .map(parameter -> buildQueryStringOfParameter(parameter.getKey(), parameter.getValue()))
                .filter(keyValue -> !keyValue.isEmpty())
                .collect(joining("&"));
    }

    private static String buildQueryStringOfParameter(final String key, final List<String> values) {
        if (values.isEmpty()) {
            return "";
        } else {
            return values.stream()
                    .map(QueryStringUtils::encode)
                    .collect(joining("&" + key + "=", key + "=", ""));
        }
    }

    /**
     * URL-encodes the given value via {@link URLEncoder#encode(String, String)}.
     * @param value the value to encode
     * @return the URL-encoded value
     */
    private static String encode(final String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            throw new IllegalArgumentException("Couldn't encode value", e);
        }
    }
}
