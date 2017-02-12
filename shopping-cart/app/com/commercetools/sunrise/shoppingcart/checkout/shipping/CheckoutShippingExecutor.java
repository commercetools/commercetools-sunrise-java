package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultCheckoutShippingExecutor.class)
@FunctionalInterface
public interface CheckoutShippingExecutor extends BiFunction<ShippingMethodsWithCart, CheckoutShippingFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(ShippingMethodsWithCart shippingMethodsWithCart, CheckoutShippingFormData formData);
}
