package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.framework.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@FunctionalInterface
@ImplementedBy(DefaultWishlistCreator.class)
public interface WishlistCreator extends ResourceFinder, Supplier<CompletionStage<ShoppingList>> {
    /**
     * Creates a wishlist.
     *
     * @return the created wishlist
     */
    CompletionStage<ShoppingList> get();
}
