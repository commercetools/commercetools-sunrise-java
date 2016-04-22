package common.utils;

import play.mvc.Http;

import java.util.List;
import java.util.Map;

import static common.utils.ArrayUtils.arrayToList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toMap;

public final class UrlUtils {

    private UrlUtils() {
    }

    public static String buildUrl(final String path, final Map<String, List<String>> queryString) {
        return buildUrl(path, buildQueryString(queryString));
    }

    public static String buildUrl(final String path, final String queryString) {
        return path + (queryString.isEmpty() ? "" : "?" + queryString);
    }

    public static String buildQueryString(final Map<String, List<String>> queryString) {
        return queryString.entrySet().stream()
                .map(parameter -> buildQueryStringOfParameter(parameter.getKey(), parameter.getValue()))
                .collect(joining("&"));
    }

    private static String buildQueryStringOfParameter(final String key, final List<String> values) {
        return values.stream().collect(joining("&" + key + "=", key + "=", ""));
    }

    public static Map<String, List<String>> getQueryString(final Http.Request request) {
        return request.queryString().entrySet().stream()
                .collect(toMap(Map.Entry::getKey, e -> arrayToList(e.getValue())));
    }
}
