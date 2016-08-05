package com.commercetools.sunrise.hooks.events;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.orders.Order;

import java.util.concurrent.CompletionStage;

public interface OrderLoadedHook extends EventHook {

    CompletionStage<?> onOrderLoaded(final Order order);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Order order) {
        return hookRunner.runEventHook(OrderLoadedHook.class, hook -> hook.onOrderLoaded(order));
    }
}