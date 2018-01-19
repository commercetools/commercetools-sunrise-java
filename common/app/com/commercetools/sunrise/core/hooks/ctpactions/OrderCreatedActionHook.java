package com.commercetools.sunrise.core.hooks.ctpactions;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import io.sphere.sdk.orders.Order;

import java.util.concurrent.CompletionStage;

public interface OrderCreatedActionHook extends CtpActionHook {

    CompletionStage<Order> onOrderCreatedAction(final Order order, final ExpansionPathContainer<Order> expansionPathContainer);

    static CompletionStage<Order> runHook(final HookRunner hookRunner, final Order order, final ExpansionPathContainer<Order> expansionPathContainer) {
        return hookRunner.run(OrderCreatedActionHook.class, order, (hook, createdOrder) -> hook.onOrderCreatedAction(createdOrder, expansionPathContainer));
    }

}
