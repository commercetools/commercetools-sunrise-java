package com.commercetools.sunrise.framework.hooks.ctpevents;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.orders.Order;

import java.util.concurrent.CompletionStage;

public interface OrderLoadedHook extends CtpEventHook {

    CompletionStage<?> onOrderLoaded(final Order order);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final Order order) {
        return hookRunner.runEventHook(OrderLoadedHook.class, hook -> hook.onOrderLoaded(order));
    }
}