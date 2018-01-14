package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.products.ProductWithVariant;

public interface ProductWithVariantLoadedHook extends CtpEventHook {

    void onProductVariantLoaded(final ProductWithVariant productWithVariant);

    static void runHook(final HookRunner hookRunner, final ProductWithVariant productWithVariant) {
        hookRunner.runEventHook(ProductWithVariantLoadedHook.class, hook -> hook.onProductVariantLoaded(productWithVariant));
    }
}
