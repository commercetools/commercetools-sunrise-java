package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.core.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultRemoveFromWishlistFormAction.class)
@FunctionalInterface
public interface RemoveFromWishlistFormAction extends FormAction<RemoveFromWishlistFormData> {

}
