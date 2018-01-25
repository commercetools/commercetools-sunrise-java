package com.commercetools.sunrise.shoppingcart.carts;

import io.sphere.sdk.carts.LineItemDraft;
import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultAddToCartFormData extends Base implements AddToCartFormData {

    @Constraints.Required
    @Constraints.MinLength(1)
    private String productId;

    @Constraints.Required
    @Constraints.Min(1)
    private Integer variantId;

    @Constraints.Required
    @Constraints.Min(1)
    private Long quantity;

    @Override
    public LineItemDraft lineItemDraft() {
        return LineItemDraft.of(productId, variantId, quantity);
    }
}
