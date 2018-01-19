package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface ShoppingListUpdateCommandHook extends FilterHook {

    CompletionStage<ShoppingList> on(ShoppingListUpdateCommand request, Function<ShoppingListUpdateCommand, CompletionStage<ShoppingList>> nextComponent);

    static CompletionStage<ShoppingList> run(final HookRunner hookRunner, final ShoppingListUpdateCommand request, final Function<ShoppingListUpdateCommand, CompletionStage<ShoppingList>> execution) {
        return hookRunner.run(ShoppingListUpdateCommandHook.class, request, execution, h -> h::on);
    }
}
