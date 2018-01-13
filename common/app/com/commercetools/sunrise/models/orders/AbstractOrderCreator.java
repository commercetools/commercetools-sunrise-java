package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.AbstractResourceCreator;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.OrderCreatedActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.OrderCreatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.OrderFromCartCreateCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.OrderFromCartDraft;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;

import java.util.concurrent.CompletionStage;

public abstract class AbstractOrderCreator extends AbstractResourceCreator<Order, OrderFromCartDraft, OrderFromCartCreateCommand> implements OrderCreator {

    protected AbstractOrderCreator(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    protected OrderFromCartCreateCommand buildRequest(final OrderFromCartDraft draft) {
        return OrderFromCartCreateCommand.of(draft);
    }

    @Override
    protected OrderFromCartCreateCommand runCreateCommandHook(final HookRunner hookRunner, final OrderFromCartCreateCommand baseCommand) {
        return OrderFromCartCreateCommandHook.runHook(hookRunner, baseCommand);
    }

    @Override
    protected CompletionStage<?> runCreatedHook(final HookRunner hookRunner, final Order resource) {
        return OrderCreatedHook.runHook(hookRunner, resource);
    }

    @Override
    protected CompletionStage<Order> runActionHook(final HookRunner hookRunner, final Order resource, final ExpansionPathContainer<Order> expansionPathContainer) {
        return OrderCreatedActionHook.runHook(hookRunner, resource, expansionPathContainer);
    }
}
