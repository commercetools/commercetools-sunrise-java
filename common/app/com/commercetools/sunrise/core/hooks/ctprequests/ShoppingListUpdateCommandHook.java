package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;

import java.util.concurrent.CompletionStage;

public interface ShoppingListUpdateCommandHook extends CtpRequestHook {

    CompletionStage<ShoppingListUpdateCommand> onShoppingListUpdateCommand(ShoppingListUpdateCommand command);

    static CompletionStage<ShoppingListUpdateCommand> runHook(final HookRunner hookRunner, final ShoppingListUpdateCommand command) {
        return hookRunner.runActionHook(ShoppingListUpdateCommandHook.class, ShoppingListUpdateCommandHook::onShoppingListUpdateCommand, command);
    }
}
