package com.commercetools.sunrise.wishlist.add;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
@ImplementedBy(DefaultAddToWishlistFormAction.class)
public interface AddToWishlistFormAction extends FormAction {

    /**
     * Adds a line item to the given wishlist.
     *
     * @param addToWishlistFormData specifies the line item to add
     *
     * @return the completion stage for the updated wishlist with the added line item
     */
    CompletionStage<ShoppingList> apply(AddToWishlistFormData addToWishlistFormData);
}
