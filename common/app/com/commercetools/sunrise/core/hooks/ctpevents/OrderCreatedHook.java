package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.Order;

import java.util.concurrent.CompletionStage;

public interface OrderCreatedHook extends CtpEventHook {

    CompletionStage<?> onOrderCreated(final Order order);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Order order) {
        return hookRunner.runEventHook(OrderCreatedHook.class, hook -> hook.onOrderCreated(order));
    }
}
