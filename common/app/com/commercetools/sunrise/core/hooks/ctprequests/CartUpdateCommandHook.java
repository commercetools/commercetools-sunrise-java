package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.commands.CartUpdateCommand;

import java.util.concurrent.CompletionStage;

public interface CartUpdateCommandHook extends CtpRequestHook {

    CompletionStage<CartUpdateCommand> onCartUpdateCommand(final CartUpdateCommand command);

    static CompletionStage<CartUpdateCommand> runHook(final HookRunner hookRunner, final CartUpdateCommand command) {
        return hookRunner.runActionHook(CartUpdateCommandHook.class, CartUpdateCommandHook::onCartUpdateCommand, command);
    }
}
