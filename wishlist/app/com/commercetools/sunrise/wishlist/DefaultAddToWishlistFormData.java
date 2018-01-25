package com.commercetools.sunrise.wishlist;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.shoppinglists.LineItemDraft;
import io.sphere.sdk.shoppinglists.LineItemDraftBuilder;
import play.data.validation.Constraints;

public class DefaultAddToWishlistFormData extends Base implements AddToWishlistFormData {

    @Constraints.Required
    @Constraints.MinLength(1)
    public String productId;

    @Constraints.Required
    @Constraints.Min(1)
    public Integer variantId;

    @Override
    public LineItemDraft lineItemDraft() {
        return LineItemDraftBuilder.of(productId).variantId(variantId).build();
    }
}
