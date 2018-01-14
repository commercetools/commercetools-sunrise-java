package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;

public interface CartLoadedHook extends CtpEventHook {

    void onCartLoaded(final Cart cart);

    static void runHook(final HookRunner hookRunner, final Cart cart) {
        hookRunner.runEventHook(CartLoadedHook.class, hook -> hook.onCartLoaded(cart));
    }
}
