package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.products.search.ProductProjectionSearch;

import java.util.concurrent.CompletionStage;

public interface ProductProjectionSearchHook extends CtpRequestHook {

    CompletionStage<ProductProjectionSearch> onProductProjectionSearch(final ProductProjectionSearch search);

    static CompletionStage<ProductProjectionSearch> runHook(final HookRunner hookRunner, final ProductProjectionSearch search) {
        return hookRunner.runActionHook(ProductProjectionSearchHook.class, ProductProjectionSearchHook::onProductProjectionSearch, search);
    }
}
