package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

public interface ProductProjectionPagedSearchResultLoadedHook extends CtpEventHook {

    void onProductProjectionPagedSearchResultLoaded(final PagedSearchResult<ProductProjection> pagedSearchResult);

    static void runHook(final HookRunner hookRunner, final PagedSearchResult<ProductProjection> pagedSearchResult) {
        hookRunner.runEventHook(ProductProjectionPagedSearchResultLoadedHook.class, hook -> hook.onProductProjectionPagedSearchResultLoaded(pagedSearchResult));
    }
}
