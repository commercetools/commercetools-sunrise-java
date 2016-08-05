package com.commercetools.sunrise.hooks.requests;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.products.search.ProductProjectionSearch;

public interface ProductProjectionSearchHook extends SphereRequestHook {

    ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch productProjectionSearch);

    static ProductProjectionSearch runHook(final HookRunner hookRunner, final ProductProjectionSearch productProjectionSearch) {
        return hookRunner.runSphereRequestHook(ProductProjectionSearchHook.class, ProductProjectionSearchHook::onProductProjectionSearch, productProjectionSearch);
    }
}
