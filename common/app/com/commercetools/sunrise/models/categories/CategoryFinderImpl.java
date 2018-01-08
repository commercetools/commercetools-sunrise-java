package com.commercetools.sunrise.models.categories;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import play.i18n.Langs;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

final class CategoryFinderImpl implements CategoryFinder {

    private final Langs langs;
    private final Locale locale;
    private final CategoryTree categoryTree;

    @Inject
    CategoryFinderImpl(final Langs langs, final Locale locale, final CategoryTree categoryTree) {
        this.langs = langs;
        this.locale = locale;
        this.categoryTree = categoryTree;
    }

    @Override
    public Optional<Category> apply(final String slug) {
        final List<Category> categories = findCategoriesMatchingSlugInAvailableLangs(slug);
        if (categories.size() > 1) {
            return categories.stream()
                    .filter(product -> categoryMatchesSlugInUsersLanguage(product, slug))
                    .findAny()
                    .map(Optional::of)
                    .orElseGet(() -> firstCategory(categories));
        } else {
            return firstCategory(categories);
        }
    }

    private List<Category> findCategoriesMatchingSlugInAvailableLangs(final String slug) {
        return langs.availables().stream()
                .map(lang -> categoryTree.findBySlug(lang.toLocale(), slug))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(toList());
    }

    private Optional<Category> firstCategory(final List<Category> categories) {
        return categories.stream().findFirst();
    }

    private boolean categoryMatchesSlugInUsersLanguage(final Category category, final String slug) {
        return category.getSlug().find(locale)
                .map(slugInUsersLang -> slugInUsersLang.equals(slug))
                .orElse(false);
    }
}
