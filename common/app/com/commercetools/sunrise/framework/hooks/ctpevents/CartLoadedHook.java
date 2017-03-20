package com.commercetools.sunrise.framework.hooks.ctpevents;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

public interface CartLoadedHook extends CtpEventHook {

    CompletionStage<?> onCartLoaded(final Cart cart);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Cart cart) {
        return hookRunner.runEventHook(CartLoadedHook.class, hook -> hook.onCartLoaded(cart));
    }
}
