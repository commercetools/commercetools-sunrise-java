package com.commercetools.sunrise.common.categorytree;

import io.sphere.sdk.categories.Category;

import java.util.Locale;
import java.util.Optional;

public final class ByNameCategoryComparator implements CategoryComparator {

    private final Locale locale;

    public ByNameCategoryComparator(Locale locale) {
        this.locale = locale;
    }

    @Override
    public int compare(Category c1, Category c2) {
        return localizedName(c1).orElse("").compareTo(localizedName(c2).orElse(""));
    }

    private Optional<String> localizedName(final Category c1) {
        return Optional.ofNullable(c1.getName().get(locale));
    }
}
