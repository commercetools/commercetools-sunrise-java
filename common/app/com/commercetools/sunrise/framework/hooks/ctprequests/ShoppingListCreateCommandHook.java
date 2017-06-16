package com.commercetools.sunrise.framework.hooks.ctprequests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;

public interface ShoppingListCreateCommandHook extends CtpRequestHook {

    ShoppingListCreateCommand onShoppingListCreateCommand(final ShoppingListCreateCommand shoppingListCreateCommand);

    static ShoppingListCreateCommand runHook(final HookRunner hookRunner, final ShoppingListCreateCommand shoppingListCreateCommand) {
        return hookRunner.runUnaryOperatorHook(ShoppingListCreateCommandHook.class, ShoppingListCreateCommandHook::onShoppingListCreateCommand, shoppingListCreateCommand);
    }
}
