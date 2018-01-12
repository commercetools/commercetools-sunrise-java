package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCheckoutPaymentFormAction.class)
@FunctionalInterface
public interface CheckoutPaymentFormAction extends FormAction {

    CompletionStage<Cart> apply(CheckoutPaymentFormData formData);
}
