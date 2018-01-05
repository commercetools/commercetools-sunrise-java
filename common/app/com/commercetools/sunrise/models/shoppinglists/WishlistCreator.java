package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.controllers.ResourceCreator;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@ImplementedBy(DefaultWishlistCreator.class)
@FunctionalInterface
public interface WishlistCreator extends ResourceCreator<ShoppingList>, Supplier<CompletionStage<ShoppingList>> {
    /**
     * Creates a wishlist.
     *
     * @return the created wishlist
     */
    CompletionStage<ShoppingList> get();
}
