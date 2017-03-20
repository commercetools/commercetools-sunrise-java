package com.commercetools.sunrise.framework.checkout.thankyou;

import com.commercetools.sunrise.framework.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@ImplementedBy(OrderCreatedFinderBySession.class)
@FunctionalInterface
public interface OrderCreatedFinder extends ResourceFinder, Supplier<CompletionStage<Optional<Order>>> {

    @Override
    CompletionStage<Optional<Order>> get();
}
