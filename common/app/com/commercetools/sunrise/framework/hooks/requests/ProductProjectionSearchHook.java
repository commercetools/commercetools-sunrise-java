package com.commercetools.sunrise.framework.hooks.requests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.products.search.ProductProjectionSearch;

public interface ProductProjectionSearchHook extends RequestHook {

    ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch productProjectionSearch);

    static ProductProjectionSearch runHook(final HookRunner hookRunner, final ProductProjectionSearch productProjectionSearch) {
        return hookRunner.runUnaryOperatorHook(ProductProjectionSearchHook.class, ProductProjectionSearchHook::onProductProjectionSearch, productProjectionSearch);
    }
}
