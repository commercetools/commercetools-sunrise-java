package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.ResourceCreator;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultOrderCreator.class)
public interface OrderCreator extends ResourceCreator {

    CompletionStage<Order> get();
}
