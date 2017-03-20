package com.commercetools.sunrise.search;

import java.util.Locale;

public final class SearchUtils {

    private SearchUtils() {
    }

    /**
     * Replaces all {@code {{locale}}} references in the expression with the given {@link Locale} language tag.
     * @param expression the expression that may contain locale references
     * @param locale the locale to be used when replacing the references
     * @return the new expression with the language tags instead of the locale references.
     */
    public static String localizeExpression(final String expression, final Locale locale) {
        return expression.replaceAll("\\{\\{locale\\}\\}", locale.toLanguageTag());
    }
}
