package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;

public interface OrderFromCartCreateCommandHook extends CtpRequestHook {

    OrderFromCartCreateCommand onOrderFromCartCreateCommand(final OrderFromCartCreateCommand orderFromCartCreateCommand);

    static OrderFromCartCreateCommand runHook(final HookRunner hookRunner, final OrderFromCartCreateCommand orderFromCartCreateCommand) {
        return hookRunner.runUnaryOperatorHook(OrderFromCartCreateCommandHook.class, OrderFromCartCreateCommandHook::onOrderFromCartCreateCommand, orderFromCartCreateCommand);
    }
}
