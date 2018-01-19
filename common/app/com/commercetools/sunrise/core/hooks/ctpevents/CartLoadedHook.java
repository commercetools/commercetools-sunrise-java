package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;

@FunctionalInterface
public interface CartLoadedHook extends ConsumerHook {

    void onLoaded(Cart cart);

    static void run(final HookRunner hookRunner, final Cart cart) {
        hookRunner.run(CartLoadedHook.class, h -> h.onLoaded(cart));
    }
}
