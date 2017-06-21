package com.commercetools.sunrise.shoppingcart.changequantity;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

public class DefaultChangeQuantityInCartFormData extends Base implements ChangeQuantityInCartFormData {

    @Required
    @MinLength(1)
    private String lineItemId;
    @Min(1)
    @Required
    private Long quantity;

    @Override
    public String lineItemId() {
        return lineItemId;
    }

    @Override
    public Long quantity() {
        return quantity;
    }


    // Getters & setters

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(final String lineItemId) {
        this.lineItemId = lineItemId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(final Long quantity) {
        this.quantity = quantity;
    }
}
