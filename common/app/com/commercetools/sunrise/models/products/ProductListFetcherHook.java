package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface ProductListFetcherHook extends FilterHook {

    CompletionStage<PagedSearchResult<ProductProjection>> on(ProductProjectionSearch request, Function<ProductProjectionSearch, CompletionStage<PagedSearchResult<ProductProjection>>> nextComponent);
}
