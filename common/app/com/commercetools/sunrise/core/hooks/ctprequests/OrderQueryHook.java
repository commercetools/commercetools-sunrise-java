package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.queries.OrderQuery;

public interface OrderQueryHook extends CtpRequestHook {

    OrderQuery onOrderQuery(final OrderQuery orderQuery);

    static OrderQuery runHook(final HookRunner hookRunner, final OrderQuery orderQuery) {
        return hookRunner.runUnaryOperatorHook(OrderQueryHook.class, OrderQueryHook::onOrderQuery, orderQuery);
    }
}
