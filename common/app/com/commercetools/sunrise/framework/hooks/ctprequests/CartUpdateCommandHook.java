package com.commercetools.sunrise.framework.hooks.ctprequests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.carts.commands.CartUpdateCommand;

public interface CartUpdateCommandHook extends CtpRequestHook {

    CartUpdateCommand onCartUpdateCommand(final CartUpdateCommand cartUpdateCommand);

    static CartUpdateCommand runHook(final HookRunner hookRunner, final CartUpdateCommand cartUpdateCommand) {
        return hookRunner.runUnaryOperatorHook(CartUpdateCommandHook.class, CartUpdateCommandHook::onCartUpdateCommand, cartUpdateCommand);
    }
}
