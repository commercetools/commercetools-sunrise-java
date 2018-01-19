package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;

@FunctionalInterface
public interface CartUpdatedHook extends ConsumerHook {

    void onUpdated(Cart cart);

    static void run(final HookRunner hookRunner, final Cart cart) {
        hookRunner.run(CartUpdatedHook.class, h -> h.onUpdated(cart));
    }
}
