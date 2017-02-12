package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.orders.Order;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultCheckoutConfirmationExecutor.class)
@FunctionalInterface
public interface CheckoutConfirmationExecutor extends BiFunction<Cart, CheckoutConfirmationFormData, CompletionStage<Order>> {

    @Override
    CompletionStage<Order> apply(Cart cart, CheckoutConfirmationFormData formData);
}
