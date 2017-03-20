package com.commercetools.sunrise.framework.hooks.ctpevents;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

/**
 * Hook called for components with read access to the cart. The cart should not be changed here.
 */
public interface CartUpdatedHook extends CtpEventHook {

    CompletionStage<?> onCartUpdated(final Cart cart);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Cart cart) {
        return hookRunner.runEventHook(CartUpdatedHook.class, hook -> hook.onCartUpdated(cart));
    }
}
