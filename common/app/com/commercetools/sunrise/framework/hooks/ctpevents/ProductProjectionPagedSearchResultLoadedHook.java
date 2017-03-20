package com.commercetools.sunrise.framework.hooks.ctpevents;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.concurrent.CompletionStage;

public interface ProductProjectionPagedSearchResultLoadedHook extends CtpEventHook {

    CompletionStage<?> onProductProjectionPagedSearchResultLoaded(final PagedSearchResult<ProductProjection> productProjectionPagedSearchResult); // Why was it returning void?

    static CompletionStage<?> runHook(final HookRunner hookRunner, final PagedSearchResult<ProductProjection> productProjectionPagedSearchResult) {
        return hookRunner.runEventHook(ProductProjectionPagedSearchResultLoadedHook.class, hook -> hook.onProductProjectionPagedSearchResultLoaded(productProjectionPagedSearchResult));
    }
}
