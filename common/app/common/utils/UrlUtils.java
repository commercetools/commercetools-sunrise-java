package common.utils;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public final class UrlUtils {

    private UrlUtils() {
    }

    public static String buildUrl(final String path, final Map<String, String[]> queryString) {
        return buildUrl(path, buildQueryString(queryString));
    }

    public static String buildUrl(final String path, final String queryString) {
        return path + (queryString.isEmpty() ? "" : "?" + queryString);
    }

    public static String buildQueryString(final Map<String, String[]> queryString) {
        return queryString.entrySet().stream()
                .map(parameter -> buildQueryStringOfParameter(parameter.getKey(), parameter.getValue()))
                .collect(joining("&"));
    }

    private static String buildQueryStringOfParameter(final String key, final String[] values) {
        return Stream.of(values).collect(joining("&" + key + "=", key + "=", ""));
    }
}
