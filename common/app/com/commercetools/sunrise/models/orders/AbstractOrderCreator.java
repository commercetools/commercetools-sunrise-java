package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.AbstractHookRunner;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class AbstractOrderCreator extends AbstractHookRunner<Order, OrderFromCartCreateCommand> implements OrderCreator {

    private final SphereClient sphereClient;

    protected AbstractOrderCreator(final HookRunner hookRunner, final SphereClient sphereClient) {
        super(hookRunner);
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<Order> get() {
        return buildRequest().thenComposeAsync(request -> runHook(request, sphereClient::execute), HttpExecution.defaultContext());
    }

    @Override
    protected CompletionStage<Order> runHook(final OrderFromCartCreateCommand request,
                                             final Function<OrderFromCartCreateCommand, CompletionStage<Order>> execution) {
        return hookRunner().run(OrderCreatorHook.class, request, execution, h -> h::on);
    }

    protected abstract CompletionStage<OrderFromCartCreateCommand> buildRequest();
}
