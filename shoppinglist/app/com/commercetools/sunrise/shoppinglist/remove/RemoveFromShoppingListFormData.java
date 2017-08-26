package com.commercetools.sunrise.shoppinglist.remove;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.LineItem;
import io.sphere.sdk.shoppinglists.ShoppingList;

/**
 * The form data used to add and remove line items from the shoppinglist.
 *
 * @see ShoppingList#getLineItems()
 * @see LineItem#getId()
 */
@ImplementedBy(DefaultRemoveFromShoppingListFormData.class)
public interface RemoveFromShoppingListFormData {

    String lineItemId();
}
