package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface MyOrderListFetcherHook extends FilterHook {

    CompletionStage<PagedQueryResult<Order>> on(OrderQuery request, Function<OrderQuery, CompletionStage<PagedQueryResult<Order>>> nextComponent);
}
