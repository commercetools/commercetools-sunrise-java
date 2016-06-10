package com.commercetools.sunrise.common.contexts;

import io.sphere.sdk.models.Base;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

public class RequestContext extends Base {

    private final String path;
    private final Map<String, List<String>> queryString;

    private RequestContext(final Map<String, List<String>> queryString, final String path) {
        this.path = path;
        this.queryString = new HashMap<>(queryString);
    }

    public String getPath() {
        return path;
    }

    public Map<String, List<String>> getQueryString() {
        return queryString;
    }

    public String buildUrl(final String key, final List<String> values) {
        final HashMap<String, List<String>> copyQueryString = new HashMap<>(queryString);
        copyQueryString.put(key, values);
        return buildUrl(path, buildQueryString(copyQueryString));
    }

    public static RequestContext of(final Map<String, List<String>> queryString, final String path) {
        return new RequestContext(queryString, path);
    }

    private static String buildQueryString(final Map<String, List<String>> queryString) {
        return queryString.entrySet().stream()
                .map(parameter -> buildQueryStringOfParameter(parameter.getKey(), parameter.getValue()))
                .collect(joining("&"));
    }

    private static String buildUrl(final String path, final String queryString) {
        return path + (queryString.isEmpty() ? "" : "?" + queryString);
    }

    private static String buildQueryStringOfParameter(final String key, final List<String> values) {
        return values.stream().collect(joining("&" + key + "=", key + "=", ""));
    }
}
