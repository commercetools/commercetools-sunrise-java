package com.commercetools.sunrise.wishlist;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.LineItemDraft;

@ImplementedBy(DefaultAddToWishlistFormData.class)
public interface AddToWishlistFormData {

    LineItemDraft lineItemDraft();
}
