package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCheckoutShippingFormAction.class)
@FunctionalInterface
public interface CheckoutShippingFormAction extends FormAction {

    CompletionStage<Cart> apply(CheckoutShippingFormData formData);
}
