package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCheckoutAddressFormAction.class)
@FunctionalInterface
public interface CheckoutAddressFormAction extends FormAction {

    CompletionStage<Cart> apply(CheckoutAddressFormData formData);
}
