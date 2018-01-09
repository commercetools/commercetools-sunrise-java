package com.commercetools.sunrise.wishlist.add;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
@ImplementedBy(DefaultAddToWishlistControllerAction.class)
public interface AddToWishlistControllerAction extends ControllerAction {

    /**
     * Adds a line item to the given wishlist.
     *
     * @param addToWishlistFormData specifies the line item to add
     *
     * @return the completion stage for the updated wishlist with the added line item
     */
    CompletionStage<ShoppingList> apply(AddToWishlistFormData addToWishlistFormData);
}
