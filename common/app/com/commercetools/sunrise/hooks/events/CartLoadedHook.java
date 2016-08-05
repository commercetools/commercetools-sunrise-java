package com.commercetools.sunrise.hooks.events;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

public interface CartLoadedHook extends EventHook {

    CompletionStage<?> onCartLoaded(final Cart cart); // Why did it return CompletionStage<Object>?

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Cart cart) {
        return hookRunner.runEventHook(CartLoadedHook.class, hook -> hook.onCartLoaded(cart));
    }
}
