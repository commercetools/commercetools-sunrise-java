package com.commercetools.sunrise.shoppinglist;

import io.sphere.sdk.shoppinglists.ShoppingList;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * This interface describes the execution flow with a required shoppinglist.
 *
 * It acts as a template class and an implementer has to provide an implementation
 * for the {@link #getShoppingListFinder()} and {@link #handleNotFoundShoppingList()}.
 *
 * Overriding the {@link #handleNotFoundShoppingList()} allows the implementer to create
 * a wishlist and thus allows to postpone the creation of a shoppinglist until it's really required
 * (e.g. when the user adds the first line item to his shoppinglist).
 */
public interface WithRequiredShoppingList {
    /**
     * @return the shoppinglist finder
     */
    ShoppingListFinder getShoppingListFinder();

    /**
     * This template method tries to get the shoppinglist via the {@link #getShoppingListFinder()}.
     *
     * If the shoppinglist could be retrieved, the given next action is called.
     *
     * Otherwise the {@link #handleNotFoundShoppingList()} method is called.
     *
     * @param nextAction the next action to call if a shoppinglist could be retrieved
     *
     * @return the result
     */
    default CompletionStage<Result> requireShoppingList(final Function<ShoppingList, CompletionStage<Result>> nextAction, final String shoppingListType) {
        return getShoppingListFinder().get(shoppingListType)
                .thenComposeAsync(wishlist -> wishlist
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundShoppingList),
                        HttpExecution.defaultContext());
    }

    /**
     * Called when no shoppinglist could be found.
     *
     * @return the result
     */
    CompletionStage<Result> handleNotFoundShoppingList();
}
