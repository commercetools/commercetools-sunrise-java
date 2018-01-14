package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.products.queries.ProductProjectionQuery;

import java.util.concurrent.CompletionStage;

public interface ProductProjectionQueryHook extends CtpRequestHook {

    CompletionStage<ProductProjectionQuery> onProductProjectionQuery(final ProductProjectionQuery query);

    static CompletionStage<ProductProjectionQuery> runHook(final HookRunner hookRunner, final ProductProjectionQuery query) {
        return hookRunner.runActionHook(ProductProjectionQueryHook.class, ProductProjectionQueryHook::onProductProjectionQuery, query);
    }
}
