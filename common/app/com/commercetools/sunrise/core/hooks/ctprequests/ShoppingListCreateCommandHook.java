package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface ShoppingListCreateCommandHook extends FilterHook {

    CompletionStage<ShoppingList> on(ShoppingListCreateCommand request, Function<ShoppingListCreateCommand, CompletionStage<ShoppingList>> nextComponent);

    static CompletionStage<ShoppingList> run(final HookRunner hookRunner, final ShoppingListCreateCommand request, final Function<ShoppingListCreateCommand, CompletionStage<ShoppingList>> execution) {
        return hookRunner.run(ShoppingListCreateCommandHook.class, request, execution, h -> h::on);
    }
}
