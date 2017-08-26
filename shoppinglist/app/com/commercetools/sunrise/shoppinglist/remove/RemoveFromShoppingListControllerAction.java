package com.commercetools.sunrise.shoppinglist.remove;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@FunctionalInterface
@ImplementedBy(DefaultRemoveFromShoppingListControllerAction.class)
public interface RemoveFromShoppingListControllerAction extends ControllerAction, BiFunction<ShoppingList, RemoveFromShoppingListFormData, CompletionStage<ShoppingList>> {
    /**
     * Removes a line item from the given wishlist.
     *
     * @param shoppingList               the {@link ShoppingList} from which to remove the line items
     * @param removeWishlistFormData specifies the line item to remove
     *
     * @return the completion stage for the updated wishlist with the removed line item
     */
    @Override
    CompletionStage<ShoppingList> apply(ShoppingList shoppingList, RemoveFromShoppingListFormData removeWishlistFormData);
}
