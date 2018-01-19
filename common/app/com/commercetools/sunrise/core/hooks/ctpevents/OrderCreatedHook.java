package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.Order;

@FunctionalInterface
public interface OrderCreatedHook extends ConsumerHook {

    void onCreated(Order order);

    static void run(final HookRunner hookRunner, final Order resource) {
        hookRunner.run(OrderCreatedHook.class, h -> h.onCreated(resource));
    }
}
