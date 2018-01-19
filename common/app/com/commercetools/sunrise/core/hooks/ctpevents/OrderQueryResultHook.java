package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.queries.PagedQueryResult;

@FunctionalInterface
public interface OrderQueryResultHook extends ConsumerHook {

    void onLoaded(PagedQueryResult<Order> orderPagedQueryResult);

    static void run(final HookRunner hookRunner, final PagedQueryResult<Order> resource) {
        hookRunner.run(OrderQueryResultHook.class, h -> h.onLoaded(resource));
    }
}
