package com.commercetools.sunrise.hooks;

import com.commercetools.sunrise.hooks.Hook;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

public interface CartLoadedHook extends Hook {
    CompletionStage<Object> cartLoaded(final Cart cart);
}
