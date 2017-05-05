package com.commercetools.sunrise.search;

import io.sphere.sdk.categories.CategoryTree;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SearchUtils {

    private static final Pattern PATTERN_LOCALE = Pattern.compile("\\{\\{locale\\}\\}");
    private static final Pattern PATTERN_EXTERNAL_ID = Pattern.compile("\\{\\{externalId=(?<externalId>[^\\}]*)\\}\\}");

    private SearchUtils() {
    }

    /**
     * Replaces all {@code {{locale}}} references in the expression with the given {@link Locale} language tag.
     * @param expression the expression that may contain locale references
     * @param locale the locale to be used when replacing the references
     * @return the new expression with the language tags instead of the locale references.
     */
    public static String localizeExpression(final String expression, final Locale locale) {
        return PATTERN_LOCALE.matcher(expression).replaceAll(locale.toLanguageTag());
    }

    /**
     * Replaces all {@code {{externalId=foo}}} references in the expression with the corresponding category ID,
     * being {@code foo} the external ID of the category to be replaced by its ID.
     * @param expression the expression that may contain external ID references
     * @param categoryTree the category tree where to find the categories
     * @return the new expression with the category IDs instead of the external ID references references.
     * @throws IllegalArgumentException when one referenced external ID does not correspond to an existing category
     */
    public static String replaceCategoryExternalId(final String expression, final CategoryTree categoryTree) {
        final Matcher matcher = PATTERN_EXTERNAL_ID.matcher(expression);
        final StringBuffer buffer = new StringBuffer(expression.length());
        while (matcher.find()) {
            final String externalId = matcher.group("externalId");
            categoryTree.findByExternalId(externalId)
                    .map(category -> matcher.appendReplacement(buffer, Matcher.quoteReplacement(category.getId())))
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Category with external ID \"%s\" not found in expression \"%s\"", externalId, expression)));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }
}
