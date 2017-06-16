package com.commercetools.sunrise.wishlist;

import io.sphere.sdk.shoppinglists.ShoppingList;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

/**
 * This interface describes the execution flow with a required wishlist.
 *
 * It acts as a template class and an implementer has to provide an implementation
 * for the {@link #getWishlistFinder()} and {@link #handleNotFoundWishlist()}.
 *
 * Overriding the {@link #handleNotFoundWishlist()} allows the implementer to create
 * a wishlist and thus allows to postpone the creation of a wishlist until it's really required
 * (e.g. when the user adds the first line item to his wishlist).
 */
public interface WithRequiredWishlist {
    /**
     * @return the wishlist finder
     */
    WishlistFinder getWishlistFinder();

    /**
     * This template method tries to get the wishlist via the {@link #getWishlistFinder()}.
     *
     * If the wishlist could be retrieved, the given next action is called.
     *
     * Otherwise the {@link #handleNotFoundWishlist()} method is called.
     *
     * @param nextAction the next action to call if a wishlist could be retrieved
     *
     * @return the result
     */
    default CompletionStage<Result> requireWishlist(final Function<ShoppingList, CompletionStage<Result>> nextAction) {
        return getWishlistFinder().get()
                .thenComposeAsync(wishlist -> wishlist
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundWishlist),
                        HttpExecution.defaultContext());
    }

    /**
     * Called when no wishlist could be found.
     *
     * @return the result
     */
    CompletionStage<Result> handleNotFoundWishlist();
}
