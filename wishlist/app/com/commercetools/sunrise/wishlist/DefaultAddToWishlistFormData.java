package com.commercetools.sunrise.wishlist;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.shoppinglists.commands.updateactions.AddLineItem;
import play.data.validation.Constraints;

public class DefaultAddToWishlistFormData extends Base implements AddToWishlistFormData {

    @Constraints.Required
    @Constraints.MinLength(1)
    private String productId;

    @Constraints.Required
    @Constraints.Min(1)
    private Integer variantId;

    @Override
    public AddLineItem addLineItem() {
        return AddLineItem.of(productId).withVariantId(variantId);
    }
}
