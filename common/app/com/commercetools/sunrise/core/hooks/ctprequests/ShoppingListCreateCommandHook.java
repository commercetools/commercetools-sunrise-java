package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;

public interface ShoppingListCreateCommandHook extends CtpRequestHook {

    ShoppingListCreateCommand onShoppingListCreateCommand(final ShoppingListCreateCommand shoppingListCreateCommand);

    static ShoppingListCreateCommand runHook(final HookRunner hookRunner, final ShoppingListCreateCommand shoppingListCreateCommand) {
        return hookRunner.runUnaryOperatorHook(ShoppingListCreateCommandHook.class, ShoppingListCreateCommandHook::onShoppingListCreateCommand, shoppingListCreateCommand);
    }
}
