package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

/**
 * Hook called for components with read access to the cart. The cart should not be changed here.
 */
public interface CartUpdatedHook extends CtpEventHook {

    void onCartUpdated(final Cart cart);

    static void runHook(final HookRunner hookRunner, final Cart cart) {
        hookRunner.runEventHook(CartUpdatedHook.class, hook -> hook.onCartUpdated(cart));
    }
}
