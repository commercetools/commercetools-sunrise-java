package com.commercetools.sunrise.wishlist.add;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;


public class DefaultAddWishlistLineItemFormData extends Base implements AddWishlistLineItemFormData {
    @Required
    @MinLength(1)
    private String productId;

    @Required
    @Min(1)
    private Integer variantId;

    @Override
    public String productId() {
        return productId;
    }

    @Override
    public Integer variantId() {
        return variantId;
    }

    // Getters & setters

    public String getProductId() {
        return productId;
    }

    public void setProductId(final String productId) {
        this.productId = productId;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(final Integer variantId) {
        this.variantId = variantId;
    }
}
