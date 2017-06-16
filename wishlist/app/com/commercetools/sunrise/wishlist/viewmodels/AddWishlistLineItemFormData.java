package com.commercetools.sunrise.wishlist.viewmodels;

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
@ImplementedBy(DefaultAddWishlistLineItemFormData.class)
public interface AddWishlistLineItemFormData {

    String productId();

    Integer variantId();
}
