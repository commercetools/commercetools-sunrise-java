package com.commercetools.sunrise.wishlist.add;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@FunctionalInterface
@ImplementedBy(DefaultAddToWishlistControllerAction.class)
public interface AddToWishlistControllerAction extends ControllerAction, BiFunction<ShoppingList, AddToWishlistFormData, CompletionStage<ShoppingList>> {
    /**
     * Adds a line item to the given wishlist.
     *
     * @param wishlist                    the {@link ShoppingList} to add the line item to
     * @param addToWishlistFormData specifies the line item to add
     *
     * @return the completion stage for the updated wishlist with the added line item
     */
    @Override
    CompletionStage<ShoppingList> apply(ShoppingList wishlist, AddToWishlistFormData addToWishlistFormData);
}
