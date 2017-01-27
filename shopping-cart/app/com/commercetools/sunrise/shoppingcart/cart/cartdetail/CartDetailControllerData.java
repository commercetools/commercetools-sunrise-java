package com.commercetools.sunrise.shoppingcart.cart.cartdetail;

import com.commercetools.sunrise.common.models.ControllerData;
import io.sphere.sdk.carts.Cart;

import javax.annotation.Nullable;

public class CartDetailControllerData extends ControllerData {

    @Nullable
    private final Cart cart;

    public CartDetailControllerData(@Nullable final Cart cart, @Nullable final Cart updatedCart) {
        this.cart = updatedCart != null ? updatedCart : cart;
    }

    @Nullable
    public Cart getCart() {
        return cart;
    }
}
