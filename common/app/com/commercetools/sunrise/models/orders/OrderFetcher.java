package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;


// TODO Consider if show order can be done differently (cache, ctx.args...)
@ImplementedBy(DefaultOrderFetcher.class)
@FunctionalInterface
public interface OrderFetcher extends ResourceFetcher<Order>, Function<String, CompletionStage<Optional<Order>>> {

    @Override
    CompletionStage<Optional<Order>> apply(String identifier);
}
