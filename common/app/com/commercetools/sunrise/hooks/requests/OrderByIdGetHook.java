package com.commercetools.sunrise.hooks.requests;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.orders.queries.OrderByIdGet;

public interface OrderByIdGetHook extends SphereRequestHook {

    OrderByIdGet onOrderByIdGet(final OrderByIdGet orderByIdGet);

    static OrderByIdGet runHook(final HookRunner hookRunner, final OrderByIdGet orderByIdGet) {
        return hookRunner.runSphereRequestHook(OrderByIdGetHook.class, OrderByIdGetHook::onOrderByIdGet, orderByIdGet);
    }
}
