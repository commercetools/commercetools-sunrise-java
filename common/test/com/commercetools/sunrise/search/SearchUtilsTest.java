package com.commercetools.sunrise.search;

import org.junit.Test;

import java.util.Locale;

import static com.commercetools.sunrise.search.SearchUtils.localizeExpression;
import static org.assertj.core.api.Assertions.assertThat;

public class SearchUtilsTest {

    @Test
    public void replacesLocale() throws Exception {
        final String expression = localizeExpression("foo.bar {{locale}} desc", Locale.ENGLISH);
        assertThat(expression).isEqualTo("foo.bar en desc");
    }

    @Test
    public void replacesMultipleLocaleOccurrences() throws Exception {
        final String expression = localizeExpression("{{locale}}.foo.{{locale}} asc", Locale.ENGLISH);
        assertThat(expression).isEqualTo("en.foo.en asc");
    }


    @Test
    public void ignoresNoOccurrences() throws Exception {
        final String expression = localizeExpression("foo.bar", Locale.ENGLISH);
        assertThat(expression).isEqualTo("foo.bar");
    }
}