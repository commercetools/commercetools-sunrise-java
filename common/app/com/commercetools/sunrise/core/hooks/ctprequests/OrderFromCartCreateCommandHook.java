package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;

import java.util.concurrent.CompletionStage;

public interface OrderFromCartCreateCommandHook extends CtpRequestHook {

    CompletionStage<OrderFromCartCreateCommand> onOrderFromCartCreateCommand(final OrderFromCartCreateCommand command);

    static CompletionStage<OrderFromCartCreateCommand> runHook(final HookRunner hookRunner, final OrderFromCartCreateCommand command) {
        return hookRunner.runActionHook(OrderFromCartCreateCommandHook.class, OrderFromCartCreateCommandHook::onOrderFromCartCreateCommand, command);
    }
}
