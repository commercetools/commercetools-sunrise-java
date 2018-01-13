package com.commercetools.sunrise.wishlist;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.commands.updateactions.AddLineItem;

@ImplementedBy(DefaultAddToWishlistFormData.class)
public interface AddToWishlistFormData {

    AddLineItem addLineItem();
}
