package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

@FunctionalInterface
public interface ProductSearchResultHook extends ConsumerHook {

    void onLoaded(PagedSearchResult<ProductProjection> searchResult);

    static void run(final HookRunner hookRunner, final PagedSearchResult<ProductProjection> resource) {
        hookRunner.run(ProductSearchResultHook.class, h -> h.onLoaded(resource));
    }
}
