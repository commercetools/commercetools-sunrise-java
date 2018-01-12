package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCheckoutConfirmationFormAction.class)
@FunctionalInterface
public interface CheckoutConfirmationFormAction extends FormAction {

    CompletionStage<Order> apply(CheckoutConfirmationFormData formData);
}
