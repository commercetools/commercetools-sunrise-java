package com.commercetools.sunrise.common.search;

import java.util.Locale;

public final class SearchUtils {

    private SearchUtils() {
    }

    public static String localizeExpression(final String expr, final Locale locale) {
        return expr.replaceAll("\\{\\{locale\\}\\}", locale.toLanguageTag());
    }
}
