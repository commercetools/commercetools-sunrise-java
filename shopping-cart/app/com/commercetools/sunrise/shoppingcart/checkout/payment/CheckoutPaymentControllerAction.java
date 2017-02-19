package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultCheckoutPaymentControllerAction.class)
@FunctionalInterface
public interface CheckoutPaymentControllerAction extends ControllerAction, BiFunction<PaymentMethodsWithCart, CheckoutPaymentFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(PaymentMethodsWithCart paymentMethodsWithCart, CheckoutPaymentFormData formData);
}
