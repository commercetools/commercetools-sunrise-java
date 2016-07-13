package com.commercetools.sunrise.hooks;

import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

public interface PrimaryCartUpdatedHook extends RequestHook {
    /**
     * Hook called for components with read access to the cart. The cart should not be changed here.
     * @param cart the current primary cart
     * @return a CompletionStage which completes when the component completes
     */
    CompletionStage<?> onPrimaryCartUpdated(final Cart cart);
}
