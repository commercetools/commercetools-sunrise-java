package com.commercetools.sunrise.framework.checkout.address;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultCheckoutAddressControllerAction.class)
@FunctionalInterface
public interface CheckoutAddressControllerAction extends ControllerAction, BiFunction<Cart, CheckoutAddressFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(Cart cart, CheckoutAddressFormData formData);
}
