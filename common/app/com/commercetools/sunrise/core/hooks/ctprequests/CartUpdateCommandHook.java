package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.commands.CartUpdateCommand;

public interface CartUpdateCommandHook extends CtpRequestHook {

    CartUpdateCommand onCartUpdateCommand(final CartUpdateCommand cartUpdateCommand);

    static CartUpdateCommand runHook(final HookRunner hookRunner, final CartUpdateCommand cartUpdateCommand) {
        return hookRunner.runUnaryOperatorHook(CartUpdateCommandHook.class, CartUpdateCommandHook::onCartUpdateCommand, cartUpdateCommand);
    }
}
