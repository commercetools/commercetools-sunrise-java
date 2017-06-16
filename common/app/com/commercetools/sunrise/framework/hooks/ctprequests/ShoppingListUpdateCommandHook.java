package com.commercetools.sunrise.framework.hooks.ctprequests;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;

public interface ShoppingListUpdateCommandHook extends CtpRequestHook {

    ShoppingListUpdateCommand onCartUpdateCommand(ShoppingListUpdateCommand shoppingListUpdateCommand);

    static ShoppingListUpdateCommand runHook(final HookRunner hookRunner, final ShoppingListUpdateCommand shoppingListUpdateCommand) {
        return hookRunner.runUnaryOperatorHook(ShoppingListUpdateCommandHook.class, ShoppingListUpdateCommandHook::onCartUpdateCommand, shoppingListUpdateCommand);
    }
}
