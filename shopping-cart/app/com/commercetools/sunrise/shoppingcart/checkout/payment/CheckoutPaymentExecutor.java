package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultCheckoutPaymentExecutor.class)
@FunctionalInterface
public interface CheckoutPaymentExecutor extends BiFunction<PaymentMethodsWithCart, CheckoutPaymentFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(PaymentMethodsWithCart paymentMethodsWithCart, CheckoutPaymentFormData formData);
}
