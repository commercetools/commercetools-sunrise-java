package com.commercetools.sunrise.shoppingcart.common;

import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;

public class CartEmptyException extends RuntimeException {
    @Nullable
    private final Cart cart;

    public CartEmptyException() {
        this(null);
    }

    public CartEmptyException(@Nullable final Cart cart) {
        this.cart = cart;
    }

    @Nullable
    public Cart getCart() {
        return cart;
    }
}
