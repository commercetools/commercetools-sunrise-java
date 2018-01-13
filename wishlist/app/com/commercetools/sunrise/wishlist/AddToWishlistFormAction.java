package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.core.FormAction;
import com.google.inject.ImplementedBy;

@ImplementedBy(DefaultAddToWishlistFormAction.class)
@FunctionalInterface
public interface AddToWishlistFormAction extends FormAction<AddToWishlistFormData> {

}
