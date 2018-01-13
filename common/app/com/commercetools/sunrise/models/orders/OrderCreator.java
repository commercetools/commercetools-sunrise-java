package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.ResourceCreator;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.OrderFromCartDraft;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultOrderCreator.class)
public interface OrderCreator extends ResourceCreator<Order, OrderFromCartDraft> {

    CompletionStage<OrderFromCartDraft> defaultDraft();

    default CompletionStage<Order> get() {
        return defaultDraft().thenComposeAsync(this::get, HttpExecution.defaultContext());
    }
}
