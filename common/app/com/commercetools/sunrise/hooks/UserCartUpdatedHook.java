package com.commercetools.sunrise.hooks;

import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

public interface UserCartUpdatedHook extends RequestHook {
    CompletionStage<?> onUserCartUpdated(final Cart cart);
}
