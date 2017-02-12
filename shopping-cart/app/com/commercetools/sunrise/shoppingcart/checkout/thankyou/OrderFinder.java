package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@ImplementedBy(DefaultOrderFinder.class)
@FunctionalInterface
public interface OrderFinder extends Supplier<CompletionStage<Optional<Order>>> {

    @Override
    CompletionStage<Optional<Order>> get();
}
