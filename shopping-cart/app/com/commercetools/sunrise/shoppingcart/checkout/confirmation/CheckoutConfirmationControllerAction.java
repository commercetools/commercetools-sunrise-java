package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCheckoutConfirmationControllerAction.class)
@FunctionalInterface
public interface CheckoutConfirmationControllerAction extends ControllerAction {

    CompletionStage<Order> apply(CheckoutConfirmationFormData formData);
}
