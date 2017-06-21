package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.framework.SunriseModel;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.payments.PaymentMethodInfo;

import java.util.List;

public final class PaymentMethodsWithCart extends SunriseModel {

    private final List<PaymentMethodInfo> paymentMethods;
    private final Cart cart;

    private PaymentMethodsWithCart(final List<PaymentMethodInfo> paymentMethods, final Cart cart) {
        this.paymentMethods = paymentMethods;
        this.cart = cart;
    }

    public List<PaymentMethodInfo> getPaymentMethods() {
        return paymentMethods;
    }

    public Cart getCart() {
        return cart;
    }

    public static PaymentMethodsWithCart of(final List<PaymentMethodInfo> paymentMethods, final Cart cart) {
        return new PaymentMethodsWithCart(paymentMethods, cart);
    }
}
