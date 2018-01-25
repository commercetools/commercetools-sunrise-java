package com.commercetools.sunrise.shoppingcart.carts;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.LineItemDraft;

@ImplementedBy(DefaultAddToCartFormData.class)
public interface AddToCartFormData {

    LineItemDraft lineItemDraft();
}
