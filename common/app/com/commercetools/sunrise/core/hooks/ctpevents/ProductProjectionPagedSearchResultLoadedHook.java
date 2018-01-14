package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

public interface ProductProjectionPagedSearchResultLoadedHook extends CtpEventHook {

    void onProductProjectionPagedSearchResultLoaded(final PagedSearchResult<ProductProjection> productProjectionPagedSearchResult); // Why was it returning void?

    static void runHook(final HookRunner hookRunner, final PagedSearchResult<ProductProjection> productProjectionPagedSearchResult) {
        hookRunner.runEventHook(ProductProjectionPagedSearchResultLoadedHook.class, hook -> hook.onProductProjectionPagedSearchResultLoaded(productProjectionPagedSearchResult));
    }
}
