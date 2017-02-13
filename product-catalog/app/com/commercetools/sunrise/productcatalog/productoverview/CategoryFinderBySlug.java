package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.events.CategoryLoadedHook;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

final class CategoryFinderBySlug implements CategoryFinder {

    private final Locale locale;
    private final CategoryTree categoryTree;
    private final HookContext hookContext;

    @Inject
    CategoryFinderBySlug(final Locale locale, final CategoryTree categoryTree, final HookContext hookContext) {
        this.locale = locale;
        this.categoryTree = categoryTree;
        this.hookContext = hookContext;
    }

    @Override
    public CompletionStage<Optional<Category>> apply(final String slug) {
        final Optional<Category> categoryOpt = categoryTree.findBySlug(locale, slug)
                .map(category -> {
                    CategoryLoadedHook.runHook(hookContext, category);
                    return category;
                });
        return completedFuture(categoryOpt);
    }
}
