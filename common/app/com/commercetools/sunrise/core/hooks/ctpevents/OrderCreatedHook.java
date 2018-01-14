package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.Order;

public interface OrderCreatedHook extends CtpEventHook {

    void onOrderCreated(final Order order);

    static void runHook(final HookRunner hookRunner, final Order order) {
        hookRunner.runEventHook(OrderCreatedHook.class, hook -> hook.onOrderCreated(order));
    }
}
