package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface MyWishlistCreatorHook extends FilterHook {

    CompletionStage<ShoppingList> on(ShoppingListCreateCommand request, Function<ShoppingListCreateCommand, CompletionStage<ShoppingList>> nextComponent);
}
