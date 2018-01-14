package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

public interface OrderPagedQueryResultLoadedHook extends CtpEventHook {

    void onOrderPagedQueryResultLoaded(final PagedQueryResult<Order> orderPagedQueryResult);

    static void runHook(final HookRunner hookRunner, final PagedQueryResult<Order> orderPagedQueryResult) {
        hookRunner.runEventHook(OrderPagedQueryResultLoadedHook.class, hook -> hook.onOrderPagedQueryResultLoaded(orderPagedQueryResult));
    }
}
