package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.framework.SunriseModel;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.shippingmethods.ShippingMethod;

import java.util.List;

public final class ShippingMethodsWithCart extends SunriseModel {

    private final List<ShippingMethod> shippingMethods;
    private final Cart cart;

    private ShippingMethodsWithCart(final List<ShippingMethod> shippingMethods, final Cart cart) {
        this.shippingMethods = shippingMethods;
        this.cart = cart;
    }

    public List<ShippingMethod> getShippingMethods() {
        return shippingMethods;
    }

    public Cart getCart() {
        return cart;
    }

    public static ShippingMethodsWithCart of(final List<ShippingMethod> shippingMethods, final Cart cart) {
        return new ShippingMethodsWithCart(shippingMethods, cart);
    }
}
