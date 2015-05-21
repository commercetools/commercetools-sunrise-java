package common.controllers;

import io.sphere.sdk.categories.Category;

import java.util.Comparator;
import java.util.Locale;
import java.util.Optional;

final class ByNameCategoryComparator implements Comparator<Category> {
    private final Locale locale;

    public ByNameCategoryComparator(Locale locale) {
        this.locale = locale;
    }

    @Override
    public int compare(Category c1, Category c2) {
        return localizedName(c1).orElse("").compareTo(localizedName(c2).orElse(""));
    }

    private Optional<String> localizedName(final Category c1) {
        return c1.getName().get(locale);
    }
}
