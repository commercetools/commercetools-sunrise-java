package com.commercetools.sunrise.wishlist.remove;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@FunctionalInterface
@ImplementedBy(DefaultRemoveFromWishlistControllerAction.class)
public interface RemoveFromWishlistControllerAction extends ControllerAction, BiFunction<ShoppingList, RemoveFromWishlistFormData, CompletionStage<ShoppingList>> {
    /**
     * Removes a line item from the given wishlist.
     *
     * @param wishlist               the {@link ShoppingList} from which to remove the line items
     * @param removeWishlistFormData specifies the line item to remove
     *
     * @return the completion stage for the updated wishlist with the removed line item
     */
    @Override
    CompletionStage<ShoppingList> apply(ShoppingList wishlist, RemoveFromWishlistFormData removeWishlistFormData);
}
