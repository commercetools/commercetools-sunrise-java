package com.commercetools.sunrise.common.contexts;

import org.junit.Test;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Arrays.asList;
import static java.util.Collections.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class RequestContextTest {

    @Test
    public void buildsUriWhenItContainsQueryString() throws Exception {
        final Map<String, List<String>> queryString = new HashMap<>();
        queryString.put("foo", asList("jacket", "shirt", "shoes"));
        queryString.put("bar", asList("1", "2"));
        queryString.put("qux", singletonList("x"));
        final String uri = RequestContext.of(queryString, "some-path")
                .buildUrl("baz", emptyList());
        final String expectedUri = "some-path?bar=1&bar=2&qux=x&foo=jacket&foo=shirt&foo=shoes";
        assertThat(uri).isEqualTo(expectedUri);
        assertThat(URLDecoder.decode(uri, "UTF-8"))
                .as("Decoded URI stays the same")
                .isEqualTo(expectedUri);
    }

    @Test
    public void buildsUriWhenEmptyQueryString() throws Exception {
        final String uri = RequestContext.of(emptyMap(), "some-path?with=multiple&query=values")
                .buildUrl("foo", asList("jacket", "shirt", "shoes"));
        final String expectedUri = "some-path?with=multiple&query=values&foo=jacket&foo=shirt&foo=shoes";
        assertThat(uri).isEqualTo(expectedUri);
        assertThat(URLDecoder.decode(uri, "UTF-8"))
                .as("Decoded URI stays the same")
                .isEqualTo(expectedUri);
    }

    @Test
    public void builtUrlShouldHaveQueryStringValuesEncoded() throws Exception {
        final Map<String, List<String>> queryString = singletonMap("foo", asList("jacket shirt", "shoes "));
        final String uri = RequestContext.of(queryString, "some-path")
                .buildUrl("bar", asList("1 2", "€√!L", "*-._~"));
        final String expectedUri = "some-path?bar=1+2&bar=%E2%82%AC%E2%88%9A%21L&bar=*-._%7E&foo=jacket+shirt&foo=shoes+";
        assertThat(uri).isEqualTo(expectedUri);
        assertThat(URLDecoder.decode(uri, "UTF-8"))
                .as("Decoded URI matches too")
                .isEqualTo("some-path?bar=1 2&bar=€√!L&bar=*-._~&foo=jacket shirt&foo=shoes ");
    }
}
