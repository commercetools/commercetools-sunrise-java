package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.CategoryLoadedHook;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;
import play.i18n.Langs;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;

public final class CategoryFinderBySlug implements CategoryFinder {

    private final Langs langs;
    private final Locale locale;
    private final CategoryTree categoryTree;
    private final HookRunner hookRunner;

    @Inject
    CategoryFinderBySlug(final Langs langs, final Locale locale, final CategoryTree categoryTree, final HookRunner hookRunner) {
        this.langs = langs;
        this.locale = locale;
        this.categoryTree = categoryTree;
        this.hookRunner = hookRunner;
    }

    @Override
    public CompletionStage<Optional<Category>> apply(final String slug) {
        final Optional<Category> categoryOpt = findCategoryBySlug(slug);
        categoryOpt.ifPresent(category -> CategoryLoadedHook.runHook(hookRunner, category));
        return completedFuture(categoryOpt);
    }

    private Optional<Category> findCategoryBySlug(final String slug) {
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
