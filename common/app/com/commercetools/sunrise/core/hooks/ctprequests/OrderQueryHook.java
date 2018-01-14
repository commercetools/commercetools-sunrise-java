package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.queries.OrderQuery;

import java.util.concurrent.CompletionStage;

public interface OrderQueryHook extends CtpRequestHook {

    CompletionStage<OrderQuery> onOrderQuery(final OrderQuery query);

    static CompletionStage<OrderQuery> runHook(final HookRunner hookRunner, final OrderQuery query) {
        return hookRunner.runActionHook(OrderQueryHook.class, OrderQueryHook::onOrderQuery, query);
    }
}
