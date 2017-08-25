package com.commercetools.sunrise.wishlist.add;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.LineItem;
import io.sphere.sdk.shoppinglists.ShoppingList;

/**
 * The form data used to add and remove line items from the wishlist.
 *
 * @see ShoppingList#getLineItems()
 * @see LineItem#getProductId()
 * @see LineItem#getVariantId()
 */
@ImplementedBy(DefaultAddToShoppingListFormData.class)
public interface AddToShoppingListFormData {

    String productId();

    Integer variantId();
}
