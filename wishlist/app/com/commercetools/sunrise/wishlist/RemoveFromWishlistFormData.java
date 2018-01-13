package com.commercetools.sunrise.wishlist;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.commands.updateactions.RemoveLineItem;

@ImplementedBy(DefaultRemoveFromWishlistFormData.class)
public interface RemoveFromWishlistFormData {

    RemoveLineItem removeLineItem();
}
