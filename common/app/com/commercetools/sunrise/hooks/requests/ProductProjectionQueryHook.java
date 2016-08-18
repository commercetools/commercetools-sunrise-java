package com.commercetools.sunrise.hooks.requests;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.products.queries.ProductProjectionQuery;

public interface ProductProjectionQueryHook extends RequestHook {

    ProductProjectionQuery onProductProjectionQuery(final ProductProjectionQuery productProjectionQuery);

    static ProductProjectionQuery runHook(final HookRunner hookRunner, final ProductProjectionQuery productProjectionQuery) {
        return hookRunner.runUnaryOperatorHook(ProductProjectionQueryHook.class, ProductProjectionQueryHook::onProductProjectionQuery, productProjectionQuery);
    }
}
