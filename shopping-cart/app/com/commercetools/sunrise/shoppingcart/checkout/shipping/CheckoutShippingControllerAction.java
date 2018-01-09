package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCheckoutShippingControllerAction.class)
@FunctionalInterface
public interface CheckoutShippingControllerAction extends ControllerAction {

    CompletionStage<Cart> apply(CheckoutShippingFormData formData);
}
