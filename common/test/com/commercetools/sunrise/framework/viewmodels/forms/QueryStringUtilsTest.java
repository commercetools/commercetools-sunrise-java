package com.commercetools.sunrise.framework.viewmodels.forms;

import org.junit.Test;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.commercetools.sunrise.framework.viewmodels.forms.FormTestUtils.someQueryString;
import static com.commercetools.sunrise.framework.viewmodels.forms.FormTestUtils.testWithHttpContext;
import static com.commercetools.sunrise.framework.viewmodels.forms.QueryStringUtils.*;
import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.assertj.core.api.Assertions.assertThat;

public class QueryStringUtilsTest {

    @Test
    public void findsAllSelectedValues() throws Exception {
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("1", "2", "3", "4"));
        testWithHttpContext(queryString, httpContext ->
                assertThat(findAllSelectedValuesFromQueryString("bar", httpContext.request()))
                        .containsExactly("1", "2", "3", "4"));
    }

    @Test
    public void findsOneSelectedValue() throws Exception {
        final Map<String, List<String>> queryString = someQueryString();
        queryString.put("bar", asList("1", "2", "3", "4"));
        testWithHttpContext(queryString, httpContext ->
                assertThat(findSelectedValueFromQueryString("bar", httpContext.request()))
                        .hasValueSatisfying(value -> assertThat(value).isIn("1", "2", "3", "4")));
    }

    @Test
    public void findsNoneIfValueNotPresent() throws Exception {
        testWithHttpContext(someQueryString(), httpContext ->
                assertThat(findAllSelectedValuesFromQueryString("bar", httpContext.request()))
                        .isEmpty());
        testWithHttpContext(someQueryString(), httpContext ->
                assertThat(findSelectedValueFromQueryString("bar", httpContext.request()))
                        .isEmpty());
    }

    @Test
    public void findsNoneIfEmptyQueryString() throws Exception {
        testWithHttpContext(emptyMap(), httpContext ->
                assertThat(findAllSelectedValuesFromQueryString("bar", httpContext.request()))
                        .isEmpty());
        testWithHttpContext(emptyMap(), httpContext ->
                assertThat(findSelectedValueFromQueryString("bar", httpContext.request()))
                        .isEmpty());
    }

    @Test
    public void buildsUriWhenItContainsQueryString() throws Exception {
        final Map<String, List<String>> queryString = new HashMap<>();
        queryString.put("foo", asList("jacket", "shirt", "shoes"));
        final String expectedUri = "some-path?with=multiple&query=values&foo=jacket&foo=shirt&foo=shoes";
        final String uri = buildUri("some-path?with=multiple&query=values", queryString);
        assertThat(uri).isEqualTo(expectedUri);
        assertThat(URLDecoder.decode(uri, "UTF-8"))
                .as("Decoded URI stays the same")
                .isEqualTo(expectedUri);
    }

    @Test
    public void buildsUri() throws Exception {
        final Map<String, List<String>> queryString = new HashMap<>();
        queryString.put("foo", asList("jacket", "shirt", "shoes"));
        queryString.put("bar", asList("1", "2"));
        queryString.put("qux", singletonList("x"));
        queryString.put("baz", emptyList());
        final String expectedUri = "some-path?bar=1&bar=2&qux=x&foo=jacket&foo=shirt&foo=shoes";
        final String uri = buildUri("some-path", queryString);
        assertThat(uri).isEqualTo(expectedUri);
        assertThat(URLDecoder.decode(uri, "UTF-8"))
                .as("Decoded URI stays the same")
                .isEqualTo(expectedUri);
    }

    @Test
    public void builtUrlShouldHaveQueryStringValuesEncoded() throws Exception {
        final Map<String, List<String>> queryString = new HashMap<>();
        queryString.put("foo", asList("jacket shirt", "shoes "));
        queryString.put("bar", asList("1 2", "€√!L", "*-._~"));
        final String expectedUri = "some-path?bar=1+2&bar=%E2%82%AC%E2%88%9A%21L&bar=*-._%7E&foo=jacket+shirt&foo=shoes+";
        final String uri = buildUri("some-path", queryString);
        assertThat(uri).isEqualTo(expectedUri);
        assertThat(URLDecoder.decode(uri, "UTF-8"))
                .as("Decoded URI matches too")
                .isEqualTo("some-path?bar=1 2&bar=€√!L&bar=*-._~&foo=jacket shirt&foo=shoes ");
    }

    @Test
    public void extractsQueryString() throws Exception {
        final Map<String, List<String>> queryString = someQueryString();
        testWithHttpContext(queryString, httpContext ->
                assertThat(extractQueryString(httpContext.request()))
                        .isEqualTo(queryString));
    }
}
