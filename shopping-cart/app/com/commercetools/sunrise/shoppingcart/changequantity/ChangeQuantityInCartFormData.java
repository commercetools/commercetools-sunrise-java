package com.commercetools.sunrise.shoppingcart.changequantity;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.commands.updateactions.ChangeLineItemQuantity;

@ImplementedBy(DefaultChangeQuantityInCartFormData.class)
public interface ChangeQuantityInCartFormData {

    default ChangeLineItemQuantity changeQuantity() {
        return ChangeLineItemQuantity.of(lineItemId(), quantity());
    }

    String lineItemId();

    Long quantity();
}
