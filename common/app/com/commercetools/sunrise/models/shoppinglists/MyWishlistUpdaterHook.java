package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface MyWishlistUpdaterHook extends FilterHook {

    CompletionStage<ShoppingList> on(ShoppingListUpdateCommand request, Function<ShoppingListUpdateCommand, CompletionStage<ShoppingList>> nextComponent);
}
