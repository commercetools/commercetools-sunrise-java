package com.commercetools.sunrise.shoppingcart.carts;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.AddLineItem;

@ImplementedBy(DefaultAddToCartFormData.class)
public interface AddToCartFormData {

    AddLineItem addLineItem();
}
