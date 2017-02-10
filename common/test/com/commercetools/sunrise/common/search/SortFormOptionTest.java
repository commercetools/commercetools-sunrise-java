package com.commercetools.sunrise.common.search;

import com.commercetools.sunrise.common.search.sort.SortFormOption;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.SearchExpression;
import io.sphere.sdk.search.SortExpression;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class SortFormOptionTest {

    @Test
    public void localizesSortExpressions() throws Exception {
        final String expr1 = "{{locale}}.foo.{{locale}} asc";
        final String expr2 = "foo.bar {{locale}} desc";
        final String expr3 = "foo.bar";
        final SortFormOption formOption = sortOption(expr1, expr2, expr3);
        assertThat(formOption.getValue())
                .extracting(SearchExpression::expression)
                .containsExactly(expr1, expr2, expr3);
        assertThat(formOption.getLocalizedValue(Locale.ENGLISH))
                .extracting(SearchExpression::expression)
                .containsExactly("en.foo.en asc", "foo.bar en desc", "foo.bar");
    }

    private static SortFormOption sortOption(final String ... expressions) {
        final List<SortExpression<ProductProjection>> sortExpressions = Arrays.stream(expressions)
                .map(SortExpression::<ProductProjection>of)
                .collect(toList());
        return new SortFormOption("label", "value", sortExpressions, false);
    }
}
