package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface ProductProjectionSearchHook extends FilterHook {

    CompletionStage<PagedSearchResult<ProductProjection>> on(ProductProjectionSearch request, Function<ProductProjectionSearch, CompletionStage<PagedSearchResult<ProductProjection>>> nextComponent);

    static CompletionStage<PagedSearchResult<ProductProjection>> run(final HookRunner hookRunner, final ProductProjectionSearch request, final Function<ProductProjectionSearch, CompletionStage<PagedSearchResult<ProductProjection>>> execution) {
        return hookRunner.run(ProductProjectionSearchHook.class, request, execution, h -> h::on);
    }
}
