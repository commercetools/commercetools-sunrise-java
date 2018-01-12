package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.models.orders.OrderCreator;
import io.sphere.sdk.orders.Order;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultCheckoutConfirmationFormAction implements CheckoutConfirmationFormAction {

    private final OrderCreator orderCreator;

    @Inject
    DefaultCheckoutConfirmationFormAction(final OrderCreator orderCreator) {
        this.orderCreator = orderCreator;
    }

    @Override
    public CompletionStage<Order> apply(final CheckoutConfirmationFormData formData) {
        return orderCreator.get();
    }
}
