package com.commercetools.sunrise.hooks.requests;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.carts.commands.CartCreateCommand;

public interface CartCreateCommandHook extends RequestHook {

    CartCreateCommand onCartCreateCommand(final CartCreateCommand cartCreateCommand);

    static CartCreateCommand runHook(final HookRunner hookRunner, final CartCreateCommand cartCreateCommand) {
        return hookRunner.runUnaryOperatorHook(CartCreateCommandHook.class, CartCreateCommandHook::onCartCreateCommand, cartCreateCommand);
    }
}
