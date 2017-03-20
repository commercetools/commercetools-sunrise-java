package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.CategoryLoadedHook;
import com.commercetools.sunrise.framework.injection.RequestScoped;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

@RequestScoped
public final class CategoryFinderBySlug implements CategoryFinder {

    private final Locale locale;
    private final CategoryTree categoryTree;
    private final HookRunner hookRunner;

    @Inject
    CategoryFinderBySlug(final Locale locale, final CategoryTree categoryTree, final HookRunner hookRunner) {
        this.locale = locale;
        this.categoryTree = categoryTree;
        this.hookRunner = hookRunner;
    }

    @Override
    public CompletionStage<Optional<Category>> apply(final String slug) {
        final Optional<Category> categoryOpt = categoryTree.findBySlug(locale, slug);
        categoryOpt.ifPresent(category -> CategoryLoadedHook.runHook(hookRunner, category));
        return completedFuture(categoryOpt);
    }
}
