package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.products.ProductWithVariant;

import java.util.concurrent.CompletionStage;

public interface ProductWithVariantLoadedHook extends CtpEventHook {

    CompletionStage<?> onProductVariantLoaded(final ProductWithVariant productWithVariant);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final ProductWithVariant productWithVariant) {
        return hookRunner.runEventHook(ProductWithVariantLoadedHook.class, hook -> hook.onProductVariantLoaded(productWithVariant));
    }
}
