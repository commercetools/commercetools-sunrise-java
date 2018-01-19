package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.products.ProductWithVariant;

@FunctionalInterface
public interface ProductWithVariantLoadedHook extends ConsumerHook {

    void onLoaded(ProductWithVariant productWithVariant);

    static void run(final HookRunner hookRunner, final ProductWithVariant resource) {
        hookRunner.run(ProductWithVariantLoadedHook.class, h -> h.onLoaded(resource));
    }
}
