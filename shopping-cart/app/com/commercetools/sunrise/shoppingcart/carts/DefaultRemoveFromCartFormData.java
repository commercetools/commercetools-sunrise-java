package com.commercetools.sunrise.shoppingcart.carts;

import io.sphere.sdk.carts.commands.updateactions.RemoveLineItem;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultRemoveFromCartFormData extends Base implements RemoveFromCartFormData {

    @Constraints.Required
    @Constraints.MinLength(1)
    private String lineItemId;

    @Override
    public RemoveLineItem removeLineItem() {
        return RemoveLineItem.of(lineItemId);
    }
}
