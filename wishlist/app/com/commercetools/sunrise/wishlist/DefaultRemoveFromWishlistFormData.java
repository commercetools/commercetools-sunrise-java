package com.commercetools.sunrise.wishlist;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.shoppinglists.commands.updateactions.RemoveLineItem;
import play.data.validation.Constraints;

public class DefaultRemoveFromWishlistFormData extends Base implements RemoveFromWishlistFormData {

    @Constraints.Required
    @Constraints.MinLength(1)
    private String lineItemId;

    @Override
    public RemoveLineItem removeLineItem() {
        return RemoveLineItem.of(lineItemId);
    }
}
