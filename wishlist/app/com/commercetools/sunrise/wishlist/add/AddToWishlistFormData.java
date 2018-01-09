package com.commercetools.sunrise.wishlist.add;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.LineItem;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.updateactions.AddLineItem;

/**
 * The form data used to add and remove line items from the wishlist.
 *
 * @see ShoppingList#getLineItems()
 * @see LineItem#getProductId()
 * @see LineItem#getVariantId()
 */
@ImplementedBy(DefaultAddToWishlistFormData.class)
public interface AddToWishlistFormData {

    default AddLineItem addLineItem() {
        return AddLineItem.of(productId()).withVariantId(variantId());
    }

    String productId();

    Integer variantId();
}
