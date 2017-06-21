package com.commercetools.sunrise.wishlist.remove;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.LineItem;
import io.sphere.sdk.shoppinglists.ShoppingList;

/**
 * The form data used to add and remove line items from the wishlist.
 *
 * @see ShoppingList#getLineItems()
 * @see LineItem#getId()
 */
@ImplementedBy(DefaultRemoveWishlistLineItemFormData.class)
public interface RemoveWishlistLineItemFormData {

    String lineItemId();
}
