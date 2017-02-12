package com.commercetools.sunrise.shoppingcart.checkout.address;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultCheckoutAddressExecutor.class)
@FunctionalInterface
public interface CheckoutAddressExecutor extends BiFunction<Cart, CheckoutAddressFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(Cart cart, CheckoutAddressFormData formData);
}
