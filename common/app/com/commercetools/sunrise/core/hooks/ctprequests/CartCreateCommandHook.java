package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.commands.CartCreateCommand;

import java.util.concurrent.CompletionStage;

public interface CartCreateCommandHook extends CtpRequestHook {

    CompletionStage<CartCreateCommand> onCartCreateCommand(final CartCreateCommand command);

    static CompletionStage<CartCreateCommand> runHook(final HookRunner hookRunner, final CartCreateCommand command) {
        return hookRunner.runActionHook(CartCreateCommandHook.class, CartCreateCommandHook::onCartCreateCommand, command);
    }
}
