package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.products.ProductProjection;

@FunctionalInterface
public interface ProductLoadedHook extends ConsumerHook {

    void onLoaded(ProductProjection product);

    static void run(final HookRunner hookRunner, final ProductProjection resource) {
        hookRunner.run(ProductLoadedHook.class, h -> h.onLoaded(resource));
    }
}
