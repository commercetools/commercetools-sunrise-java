package com.commercetools.sunrise.framework.cart.addlineitem;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints.Min;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;

public class DefaultAddLineItemFormData extends Base implements AddLineItemFormData {

    @Required
    @MinLength(1)
    private String productId;
    @Required
    @Min(1)
    private Integer variantId;
    @Required
    @Min(1)
    private Long quantity;

    @Override
    public String productId() {
        return productId;
    }

    @Override
    public Integer variantId() {
        return variantId;
    }

    @Override
    public Long quantity() {
        return quantity;
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

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(final Long quantity) {
        this.quantity = quantity;
    }
}
