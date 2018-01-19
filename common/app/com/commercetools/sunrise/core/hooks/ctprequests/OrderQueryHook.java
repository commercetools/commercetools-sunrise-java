package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface OrderQueryHook extends FilterHook {

    CompletionStage<PagedQueryResult<Order>> on(OrderQuery request, Function<OrderQuery, CompletionStage<PagedQueryResult<Order>>> nextComponent);

    static CompletionStage<PagedQueryResult<Order>> run(final HookRunner hookRunner, final OrderQuery request, final Function<OrderQuery, CompletionStage<PagedQueryResult<Order>>> execution) {
        return hookRunner.run(OrderQueryHook.class, request, execution, h -> h::on);
    }
}
