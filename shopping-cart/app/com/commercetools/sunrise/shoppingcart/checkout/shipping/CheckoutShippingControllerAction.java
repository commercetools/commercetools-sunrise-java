package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultCheckoutShippingControllerAction.class)
@FunctionalInterface
public interface CheckoutShippingControllerAction extends ControllerAction, BiFunction<ShippingMethodsWithCart, CheckoutShippingFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(ShippingMethodsWithCart shippingMethodsWithCart, CheckoutShippingFormData formData);
}
