package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.orders.Order;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultCheckoutConfirmationControllerAction.class)
@FunctionalInterface
public interface CheckoutConfirmationControllerAction extends ControllerAction, BiFunction<Cart, CheckoutConfirmationFormData, CompletionStage<Order>> {

    @Override
    CompletionStage<Order> apply(Cart cart, CheckoutConfirmationFormData formData);
}
