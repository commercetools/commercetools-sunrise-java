package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import java.util.List;

public final class ShippingMethodsWithCart {

    private final List<ShippingMethod> shippingMethods;
    private final Cart cart;

    public ShippingMethodsWithCart(final List<ShippingMethod> shippingMethods, final Cart cart) {
        this.shippingMethods = shippingMethods;
        this.cart = cart;
    }

    public List<ShippingMethod> getShippingMethods() {
        return shippingMethods;
    }

    public Cart getCart() {
        return cart;
    }
}
