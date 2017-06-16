package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.framework.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@FunctionalInterface
@ImplementedBy(WishlistFinderBySession.class)
public interface WishlistFinder extends ResourceFinder, Supplier<CompletionStage<Optional<ShoppingList>>> {
    /**
     * If the current session contains a signed in customer or a previously created wishlist, this wishlist
     * will be returned. Otherwise an empty optional will be returned.
     *
     * @return the completion stage for the wishlist
     */
    CompletionStage<Optional<ShoppingList>> get();
}
