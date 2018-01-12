package com.commercetools.sunrise.models.categories;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import io.sphere.sdk.categories.CategoryTree;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class CategoryTreeDisplayingComponent implements ControllerComponent, PageDataHook {

    private final CategoryTree categoryTree;

    @Inject
    CategoryTreeDisplayingComponent(@NavigationCategoryTree final CategoryTree categoryTree) {
        this.categoryTree = categoryTree;
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return completedFuture(pageData.put("categoryTree", categoryTree));
    }
}
