package com.commercetools.sunrise.shoppingcart.common;

import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;

public class PrimaryCartEmptyException extends RuntimeException {
    @Nullable
    private final Cart cart;

    public PrimaryCartEmptyException() {
        this(null);
    }

    public PrimaryCartEmptyException(@Nullable final Cart cart) {
        this.cart = cart;
    }

    @Nullable
    public Cart getCart() {
        return cart;
    }
}
