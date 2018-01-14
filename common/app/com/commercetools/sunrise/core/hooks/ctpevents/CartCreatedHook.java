package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;

public interface CartCreatedHook extends CtpEventHook {

    void onCartCreated(final Cart cart);

    static void runHook(final HookRunner hookRunner, final Cart cart) {
        hookRunner.runEventHook(CartCreatedHook.class, hook -> hook.onCartCreated(cart));
    }
}
