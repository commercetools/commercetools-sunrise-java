package com.commercetools.sunrise.hooks.events;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

public interface CartCreatedHook extends EventHook {

    CompletionStage<?> onCartCreated(final Cart cart);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Cart cart) {
        return hookRunner.runEventHook(CartCreatedHook.class, hook -> hook.onCartCreated(cart));
    }
}
