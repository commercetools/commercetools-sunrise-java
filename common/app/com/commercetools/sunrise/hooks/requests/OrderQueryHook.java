package com.commercetools.sunrise.hooks.requests;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.orders.queries.OrderQuery;

public interface OrderQueryHook extends RequestHook {

    OrderQuery onOrderQuery(final OrderQuery orderQuery);

    static OrderQuery runHook(final HookRunner hookRunner, final OrderQuery orderQuery) {
        return hookRunner.runUnaryOperatorHook(OrderQueryHook.class, OrderQueryHook::onOrderQuery, orderQuery);
    }
}
