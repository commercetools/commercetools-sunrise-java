package com.commercetools.sunrise.hooks.events;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.concurrent.CompletionStage;

public interface ProductVariantLoadedHook extends EventHook {

    CompletionStage<?> onProductVariantLoaded(final ProductProjection productProjection, final ProductVariant productVariant);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final ProductProjection productProjection, final ProductVariant productVariant) {
        return hookRunner.runEventHook(ProductVariantLoadedHook.class, hook -> hook.onProductVariantLoaded(productProjection, productVariant));
    }
}
