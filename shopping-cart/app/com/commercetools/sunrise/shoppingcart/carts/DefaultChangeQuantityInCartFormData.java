package com.commercetools.sunrise.shoppingcart.carts;

import io.sphere.sdk.carts.commands.updateactions.ChangeLineItemQuantity;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultChangeQuantityInCartFormData extends Base implements ChangeQuantityInCartFormData {

    @Constraints.Required
    @Constraints.MinLength(1)
    private String lineItemId;

    @Constraints.Min(1)
    @Constraints.Required
    private Long quantity;

    @Override
    public ChangeLineItemQuantity changeLineItemQuantity() {
        return ChangeLineItemQuantity.of(lineItemId, quantity);
    }
}
