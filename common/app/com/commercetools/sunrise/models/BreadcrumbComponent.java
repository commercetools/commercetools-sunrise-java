package com.commercetools.sunrise.models;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.models.categories.CategoryFetcherHook;
import com.commercetools.sunrise.models.products.ProductListFetcherHook;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.categories.expansion.CategoryExpansionModel;
import io.sphere.sdk.categories.queries.CategoryQuery;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public final class BreadcrumbComponent implements ControllerComponent, CategoryFetcherHook, ProductListFetcherHook {

    @Override
    public CompletionStage<Optional<Category>> on(final CategoryQuery request, final Function<CategoryQuery, CompletionStage<Optional<Category>>> nextComponent) {
        return nextComponent.apply(request.plusExpansionPaths(CategoryExpansionModel::ancestors));
    }

    @Override
    public CompletionStage<PagedSearchResult<ProductProjection>> on(final ProductProjectionSearch request, final Function<ProductProjectionSearch, CompletionStage<PagedSearchResult<ProductProjection>>> nextComponent) {
        return nextComponent.apply(request.plusExpansionPaths(p -> p.categories(0).ancestors()));
    }
}
