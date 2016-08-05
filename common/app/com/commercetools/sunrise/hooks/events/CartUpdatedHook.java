package com.commercetools.sunrise.hooks.events;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

/**
 * Hook called for components with read access to the cart. The cart should not be changed here.
 */
public interface CartUpdatedHook extends EventHook { // Why did it extend SphereRequestHook?

    CompletionStage<?> onCartUpdated(final Cart cart);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Cart cart) {
        return hookRunner.runEventHook(CartUpdatedHook.class, hook -> hook.onCartUpdated(cart));
    }
}
