package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;

public interface OrderPagedQueryResultLoadedHook extends CtpEventHook {

    CompletionStage<?> onOrderPagedQueryResultLoaded(final PagedQueryResult<Order> orderPagedQueryResult);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final PagedQueryResult<Order> orderPagedQueryResult) {
        return hookRunner.runEventHook(OrderPagedQueryResultLoadedHook.class, hook -> hook.onOrderPagedQueryResultLoaded(orderPagedQueryResult));
    }
}
