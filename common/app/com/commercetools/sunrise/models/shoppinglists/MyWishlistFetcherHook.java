package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface MyWishlistFetcherHook extends FilterHook {

    CompletionStage<Optional<ShoppingList>> on(ShoppingListQuery request, Function<ShoppingListQuery, CompletionStage<Optional<ShoppingList>>> nextComponent);
}