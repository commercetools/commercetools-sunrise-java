package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCheckoutAddressControllerAction.class)
@FunctionalInterface
public interface CheckoutAddressControllerAction extends ControllerAction {

    CompletionStage<Cart> apply(CheckoutAddressFormData formData);
}
