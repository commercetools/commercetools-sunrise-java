package com.commercetools.sunrise.hooks;

import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

public interface PrimaryCartUpdatedHook extends RequestHook {
    CompletionStage<?> onPrimaryCartUpdated(final Cart cart);
}
