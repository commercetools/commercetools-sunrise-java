package com.commercetools.sunrise.shoppingcart.checkout.payment;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.payments.PaymentMethodInfo;

import java.util.List;

public final class PaymentMethodsWithCart {

    private final List<PaymentMethodInfo> paymentMethods;
    private final Cart cart;

    public PaymentMethodsWithCart(final List<PaymentMethodInfo> paymentMethods, final Cart cart) {
        this.paymentMethods = paymentMethods;
        this.cart = cart;
    }

    public List<PaymentMethodInfo> getPaymentMethods() {
        return paymentMethods;
    }

    public Cart getCart() {
        return cart;
    }
}
