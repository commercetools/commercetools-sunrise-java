package com.commercetools.sunrise.wishlist.remove;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

public class DefaultRemoveWishlistLineItemFormData extends Base implements RemoveWishlistLineItemFormData {
    @Required
    @MinLength(1)
    private String lineItemId;

    @Override
    public String lineItemId() {
        return lineItemId;
    }

    public String getLineItemId() {
        return lineItemId;
    }

    public void setLineItemId(final String lineItemId) {
        this.lineItemId = lineItemId;
    }
}
