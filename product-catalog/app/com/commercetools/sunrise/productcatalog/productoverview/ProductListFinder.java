package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.framework.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(ProductListFinderByCategory.class)
@FunctionalInterface
public interface ProductListFinder extends ResourceFinder, Function<Category, CompletionStage<PagedSearchResult<ProductProjection>>> {

    @Override
    CompletionStage<PagedSearchResult<ProductProjection>> apply(@Nullable final Category category);
}
