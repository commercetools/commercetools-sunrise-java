package com.commercetools.sunrise.shoppingcart.common;

import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

public interface WithCartPreconditions {
    /**
     * Method to load the cart and throw an exception if a precondition is not met to enter the action.
     * Example: an empty cart should not qualify to use checkout payment functionality.
     * @return cart stage
     */
    CompletionStage<Cart> loadCartWithPreconditions();
}
