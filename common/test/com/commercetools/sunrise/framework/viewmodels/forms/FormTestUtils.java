package com.commercetools.sunrise.framework.viewmodels.forms;

import play.mvc.Http;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.Arrays.asList;
import static play.test.Helpers.fakeRequest;

public final class FormTestUtils {

    private FormTestUtils() {
    }

    public static Map<String, List<String>> someQueryString() {
        final Map<String, List<String>> queryString = new HashMap<>();
        queryString.put("foo", asList("x", "y", "z"));
        queryString.put("qux", asList("v", "w"));
        return queryString;
    }

    public static Map<String, List<String>> quxQueryString() {
        final Map<String, List<String>> queryString = new HashMap<>();
        queryString.put("qux", asList("v", "w"));
        return queryString;
    }



    public static void testWithHttpContext(final Map<String, List<String>> queryString, final Consumer<Http.Context> test) {
        final Http.Request request = fakeRequest()
                .uri(QueryStringUtils.buildUri("path", queryString))
                .build();
        test.accept(new Http.Context(request));
    }
}
