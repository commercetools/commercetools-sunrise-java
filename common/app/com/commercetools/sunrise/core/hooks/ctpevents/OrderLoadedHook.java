package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.Order;

import java.util.concurrent.CompletionStage;

public interface OrderLoadedHook extends CtpEventHook {

    void onOrderLoaded(final Order order);

    static void runHook(final HookRunner hookRunner, final Order order) {
        hookRunner.runEventHook(OrderLoadedHook.class, hook -> hook.onOrderLoaded(order));
    }
}