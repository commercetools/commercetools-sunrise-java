package com.commercetools.sunrise.shoppinglist;

import com.commercetools.sunrise.framework.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
@ImplementedBy(DefaultShoppingListCreator.class)
public interface ShoppingListCreator extends ResourceFinder {
    /**
     * Creates a wishlist.
     *
     * @return the created wishlist
     */
    CompletionStage<ShoppingList> get(final String shoppingListType);
}
