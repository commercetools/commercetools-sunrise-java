package com.commercetools.sunrise.hooks.requests;

import com.commercetools.sunrise.hooks.HookRunner;
import io.sphere.sdk.carts.commands.CartUpdateCommand;

public interface CartUpdateCommandHook extends RequestHook {

    CartUpdateCommand onCartUpdateCommand(final CartUpdateCommand cartUpdateCommand);

    static CartUpdateCommand runHook(final HookRunner hookRunner, final CartUpdateCommand cartUpdateCommand) {
        return hookRunner.runUnaryOperatorHook(CartUpdateCommandHook.class, CartUpdateCommandHook::onCartUpdateCommand, cartUpdateCommand);
    }
}
