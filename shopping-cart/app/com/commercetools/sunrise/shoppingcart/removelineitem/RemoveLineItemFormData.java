package com.commercetools.sunrise.shoppingcart.removelineitem;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class RemoveLineItemFormData extends Base {
    @Constraints.Required
    private String lineItemId;

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(final String lineItemId) {
        this.lineItemId = lineItemId;
    }
}
