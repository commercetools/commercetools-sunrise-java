package com.commercetools.sunrise.common.categorytree;

import io.sphere.sdk.categories.Category;

import java.util.Locale;

public final class ByNameCategoryComparator implements CategoryComparator {

    private final Locale locale;

    public ByNameCategoryComparator(final Locale locale) {
        this.locale = locale;
    }

    @Override
    public int compare(final Category c1, final Category c2) {
        return localizedNameOrEmpty(c1).compareTo(localizedNameOrEmpty(c2));
    }

    private String localizedNameOrEmpty(final Category c1) {
        return c1.getName().find(locale).orElse("");
    }
}
