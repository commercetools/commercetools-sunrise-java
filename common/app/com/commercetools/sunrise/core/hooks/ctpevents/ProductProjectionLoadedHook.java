package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.products.ProductProjection;

import java.util.concurrent.CompletionStage;

public interface ProductProjectionLoadedHook extends CtpEventHook {

    CompletionStage<?> onProductProjectionLoaded(final ProductProjection productProjection);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final ProductProjection productProjection) {
        return hookRunner.runEventHook(ProductProjectionLoadedHook.class, hook -> hook.onProductProjectionLoaded(productProjection));
    }
}
