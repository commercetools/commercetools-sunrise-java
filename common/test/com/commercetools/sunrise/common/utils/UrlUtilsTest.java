package com.commercetools.sunrise.common.utils;

import org.junit.Test;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.commercetools.sunrise.common.utils.UrlUtils.buildUrl;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for {@link UrlUtils}.
 */
public class UrlUtilsTest {

    @Test
    public void buildsUrl() throws Exception {
        final Map<String, List<String>> queryString = new HashMap<>();

        queryString.put("foo", asList("jacket", "shirt", "shoes"));
        queryString.put("bar", asList("1", "2"));
        queryString.put("qux", singletonList("x"));
        queryString.put("baz", emptyList());

        final String url = buildUrl("some-path", queryString);
        final String expectedUrl = "some-path?bar=1&bar=2&qux=x&foo=jacket&foo=shirt&foo=shoes";

        assertThat(url).isEqualTo(expectedUrl);


        final String decodedUrl = URLDecoder.decode(url, "UTF-8");

        assertThat(decodedUrl).isEqualTo(expectedUrl);
    }

    @Test
    public void builtUrlShouldHaveQueryStringValuesEncoded() throws Exception {
        final Map<String, List<String>> queryString = new HashMap<>();

        queryString.put("foo", asList("jacket shirt", "shoes "));
        queryString.put("bar", asList("1 2", "€√!L", "*-._~"));
        final String url = buildUrl("some-path", queryString);

        final String expectedUri = "some-path?bar=1+2&bar=%E2%82%AC%E2%88%9A%21L&bar=*-._%7E&foo=jacket+shirt&foo=shoes+";
        assertThat(url).isEqualTo(expectedUri);

        final String decodedUrl = URLDecoder.decode(url, "UTF-8");
        assertThat(decodedUrl)
                .as("Decoded URI matches too")
                .isEqualTo("some-path?bar=1 2&bar=€√!L&bar=*-._~&foo=jacket shirt&foo=shoes ");
    }
}
