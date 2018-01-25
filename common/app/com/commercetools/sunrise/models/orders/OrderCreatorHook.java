package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface OrderCreatorHook extends FilterHook {

    CompletionStage<Order> on(OrderFromCartCreateCommand request, Function<OrderFromCartCreateCommand, CompletionStage<Order>> nextComponent);
}
