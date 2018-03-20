package com.commercetools.sunrise.common.contexts;

import io.sphere.sdk.models.Base;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public final class RequestContextImpl extends Base implements RequestContext {

    private final String path;
    private final Map<String, List<String>> queryString;

    public RequestContextImpl(final Map<String, List<String>> queryString, final String path) {
        this.path = path;
        this.queryString = new HashMap<>(queryString);
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Map<String, List<String>> getQueryString() {
        return queryString;
    }

    @Override
    public String buildUrl(final String key, final List<String> values) {
        final HashMap<String, List<String>> copyQueryString = new HashMap<>(queryString);
        copyQueryString.put(key, values);
        return buildUrl(path, copyQueryString);
    }

    /**
     * Builds a URI from the given path and the query string, of the form: {@code path?foo=1&bar=2}
     * @param path URI path without query string
     * @param queryString the query string
     * @return a URI composed by the path and query string
     */
    private static String buildUrl(final String path, final Map<String, List<String>> queryString) {
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
                    .map(RequestContextImpl::encode)
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
