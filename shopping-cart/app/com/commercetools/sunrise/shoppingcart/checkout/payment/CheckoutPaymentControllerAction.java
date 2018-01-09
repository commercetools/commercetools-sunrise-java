package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCheckoutPaymentControllerAction.class)
@FunctionalInterface
public interface CheckoutPaymentControllerAction extends ControllerAction {

    CompletionStage<Cart> apply(CheckoutPaymentFormData formData);
}
