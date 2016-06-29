package com.commercetools.sunrise.shoppingcart.changelineitemquantity;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultChangeLineItemQuantityFormData extends Base implements ChangeLineItemQuantityFormData {
    @Constraints.Required
    private String lineItemId;
    @Constraints.Min(0)
    @Constraints.Max(100)
    @Constraints.Required
    private Long quantity;

    @Override
    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(final String lineItemId) {
        this.lineItemId = lineItemId;
    }

    @Override
    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(final Long quantity) {
        this.quantity = quantity;
    }
}
