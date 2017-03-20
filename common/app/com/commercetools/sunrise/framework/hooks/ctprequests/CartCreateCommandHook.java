package com.commercetools.sunrise.framework.hooks.ctprequests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.carts.commands.CartCreateCommand;

public interface CartCreateCommandHook extends CtpRequestHook {

    CartCreateCommand onCartCreateCommand(final CartCreateCommand cartCreateCommand);

    static CartCreateCommand runHook(final HookRunner hookRunner, final CartCreateCommand cartCreateCommand) {
        return hookRunner.runUnaryOperatorHook(CartCreateCommandHook.class, CartCreateCommandHook::onCartCreateCommand, cartCreateCommand);
    }
}
