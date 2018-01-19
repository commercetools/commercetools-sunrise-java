package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface OrderFromCartCreateCommandHook extends FilterHook {

    CompletionStage<Order> on(OrderFromCartCreateCommand request, Function<OrderFromCartCreateCommand, CompletionStage<Order>> nextComponent);

    static CompletionStage<Order> run(final HookRunner hookRunner, final OrderFromCartCreateCommand request, final Function<OrderFromCartCreateCommand, CompletionStage<Order>> execution) {
        return hookRunner.run(OrderFromCartCreateCommandHook.class, request, execution, h -> h::on);
    }
}
