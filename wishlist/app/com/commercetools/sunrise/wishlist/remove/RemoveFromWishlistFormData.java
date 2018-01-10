package com.commercetools.sunrise.wishlist.remove;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.shoppinglists.LineItem;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.updateactions.RemoveLineItem;

import java.util.Collections;
import java.util.List;

/**
 * The form data used to add and remove line items from the wishlist.
 *
 * @see ShoppingList#getLineItems()
 * @see LineItem#getId()
 */
@ImplementedBy(DefaultRemoveFromWishlistFormData.class)
public interface RemoveFromWishlistFormData {

    default List<UpdateAction<ShoppingList>> updateActions() {
        return Collections.singletonList(RemoveLineItem.of(lineItemId()));
    }

    String lineItemId();
}
