package com.commercetools.sunrise.productcatalog.productoverview;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(DefaultProductListFinder.class)
@FunctionalInterface
public interface ProductListFinder extends Function<Category, CompletionStage<PagedSearchResult<ProductProjection>>> {

    @Override
    CompletionStage<PagedSearchResult<ProductProjection>> apply(@Nullable final Category category);
}
