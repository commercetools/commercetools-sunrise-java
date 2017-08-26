package com.commercetools.sunrise.shoppinglist.add;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@FunctionalInterface
@ImplementedBy(DefaultAddToSoppinglistControllerAction.class)
public interface AddToShoppingListControllerAction extends ControllerAction, BiFunction<ShoppingList, AddToShoppingListFormData, CompletionStage<ShoppingList>> {
    /**
     * Adds a line item to the given shoppinglist.
     *
     * @param shoppingList the {@link ShoppingList} to add the line item to
     * @param addToShoppingListFormData specifies the line item to add
     *
     * @return the completion stage for the updated wishlist with the added line item
     */
    @Override
    CompletionStage<ShoppingList> apply(ShoppingList shoppingList, AddToShoppingListFormData addToShoppingListFormData);
}
