package com.commercetools.sunrise.hooks.events;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.products.ProductProjection;

import java.util.concurrent.CompletionStage;

public interface ProductProjectionLoadedHook extends EventHook {

    CompletionStage<?> onProductProjectionLoaded(final ProductProjection productProjection);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final ProductProjection productProjection) {
        return hookRunner.runEventHook(ProductProjectionLoadedHook.class, hook -> hook.onProductProjectionLoaded(productProjection));
    }
}
