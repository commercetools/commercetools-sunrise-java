package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.Order;

@FunctionalInterface
public interface OrderLoadedHook extends ConsumerHook {

    void onLoaded(Order order);

    static void run(final HookRunner hookRunner, final Order resource) {
        hookRunner.run(OrderLoadedHook.class, h -> h.onLoaded(resource));
    }
}