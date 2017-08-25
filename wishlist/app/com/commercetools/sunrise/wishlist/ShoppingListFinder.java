package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.framework.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@FunctionalInterface
@ImplementedBy(ShoppingListFinderBySession.class)
public interface ShoppingListFinder extends ResourceFinder{
    /**
     * If the current session contains a signed in customer or a previously created wishlist, this wishlist
     * will be returned. Otherwise an empty optional will be returned.
     *
     * @return the completion stage for the wishlist
     */
    CompletionStage<Optional<ShoppingList>> get(final String shoppingListType);
}
