package com.commercetools.sunrise.hooks.requests;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.orders.queries.OrderByIdGet;

public interface OrderByIdGetHook extends RequestHook {

    OrderByIdGet onOrderByIdGet(final OrderByIdGet orderByIdGet);

    static OrderByIdGet runHook(final HookRunner hookRunner, final OrderByIdGet orderByIdGet) {
        return hookRunner.runUnaryOperatorHook(OrderByIdGetHook.class, OrderByIdGetHook::onOrderByIdGet, orderByIdGet);
    }
}
