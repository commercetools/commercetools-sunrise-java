package com.commercetools.sunrise.hooks.requests;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.products.queries.ProductProjectionQuery;

public interface ProductProjectionQueryHook extends SphereRequestHook {

    ProductProjectionQuery onProductProjectionQuery(final ProductProjectionQuery productProjectionQuery);

    static ProductProjectionQuery runHook(final HookRunner hookRunner, final ProductProjectionQuery productProjectionQuery) {
        return hookRunner.runSphereRequestHook(ProductProjectionQueryHook.class, ProductProjectionQueryHook::onProductProjectionQuery, productProjectionQuery);
    }
}
