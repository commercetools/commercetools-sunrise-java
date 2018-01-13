package com.commercetools.sunrise.shoppingcart.carts;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;

@ImplementedBy(DefaultRemoveFromCartFormData.class)
public interface RemoveFromCartFormData {

    RemoveLineItem removeLineItem();
}
