package com.commercetools.sunrise.models;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.ctprequests.CategoryQueryHook;
import com.commercetools.sunrise.core.hooks.ctprequests.ProductProjectionQueryHook;
import io.sphere.sdk.categories.expansion.CategoryExpansionModel;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.products.queries.ProductProjectionQuery;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class BreadcrumbComponent implements ControllerComponent, CategoryQueryHook, ProductProjectionQueryHook {

    @Override
    public CompletionStage<CategoryQuery> onCategoryQuery(final CategoryQuery query) {
        return completedFuture(query.plusExpansionPaths(CategoryExpansionModel::ancestors));
    }

    @Override
    public CompletionStage<ProductProjectionQuery> onProductProjectionQuery(final ProductProjectionQuery query) {
        return completedFuture(query.plusExpansionPaths(p -> p.categories(0).ancestors()));
    }
}
