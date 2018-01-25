package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface MyOrderFetcherHook extends FilterHook {

    CompletionStage<Optional<Order>> on(OrderQuery request, Function<OrderQuery, CompletionStage<Optional<Order>>> nextComponent);
}
