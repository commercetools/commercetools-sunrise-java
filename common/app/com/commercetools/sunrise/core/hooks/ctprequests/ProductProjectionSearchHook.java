package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.products.search.ProductProjectionSearch;

public interface ProductProjectionSearchHook extends CtpRequestHook {

    ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch productProjectionSearch);

    static ProductProjectionSearch runHook(final HookRunner hookRunner, final ProductProjectionSearch productProjectionSearch) {
        return hookRunner.runUnaryOperatorHook(ProductProjectionSearchHook.class, ProductProjectionSearchHook::onProductProjectionSearch, productProjectionSearch);
    }
}
