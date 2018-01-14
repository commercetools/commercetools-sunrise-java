package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;

import java.util.concurrent.CompletionStage;

public interface ShoppingListCreateCommandHook extends CtpRequestHook {

    CompletionStage<ShoppingListCreateCommand> onShoppingListCreateCommand(final ShoppingListCreateCommand command);

    static CompletionStage<ShoppingListCreateCommand> runHook(final HookRunner hookRunner, final ShoppingListCreateCommand command) {
        return hookRunner.runActionHook(ShoppingListCreateCommandHook.class, ShoppingListCreateCommandHook::onShoppingListCreateCommand, command);
    }
}
