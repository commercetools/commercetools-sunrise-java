package com.commercetools.sunrise.framework.hooks.ctprequests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.products.queries.ProductProjectionQuery;

public interface ProductProjectionQueryHook extends CtpRequestHook {

    ProductProjectionQuery onProductProjectionQuery(final ProductProjectionQuery productProjectionQuery);

    static ProductProjectionQuery runHook(final HookRunner hookRunner, final ProductProjectionQuery productProjectionQuery) {
        return hookRunner.runUnaryOperatorHook(ProductProjectionQueryHook.class, ProductProjectionQueryHook::onProductProjectionQuery, productProjectionQuery);
    }
}
