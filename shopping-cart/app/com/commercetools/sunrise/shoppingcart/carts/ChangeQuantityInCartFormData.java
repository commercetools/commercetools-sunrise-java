package com.commercetools.sunrise.shoppingcart.carts;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.ChangeLineItemQuantity;

@ImplementedBy(DefaultChangeQuantityInCartFormData.class)
public interface ChangeQuantityInCartFormData {

    ChangeLineItemQuantity changeLineItemQuantity();
}
