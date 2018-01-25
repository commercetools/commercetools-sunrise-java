package com.commercetools.sunrise.wishlist;

import io.sphere.sdk.models.Base;
import io.sphere.sdk.shoppinglists.LineItemDraft;
import io.sphere.sdk.shoppinglists.LineItemDraftBuilder;
import play.data.validation.Constraints;

public class DefaultAddToWishlistFormData extends Base implements AddToWishlistFormData {

    @Constraints.Required
    @Constraints.MinLength(1)
    private String productId;

    @Constraints.Required
    @Constraints.Min(1)
    private Integer variantId;

    @Override
    public LineItemDraft lineItemDraft() {
        return LineItemDraftBuilder.of(productId).variantId(variantId).build();
    }
}
