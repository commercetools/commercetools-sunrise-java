package com.commercetools.sunrise.shoppingcart.cart.addtocart;

import io.sphere.sdk.models.Base;
import play.data.validation.Constraints;

public class DefaultAddProductToCartFormData extends Base implements AddProductToCartFormData {
    @Constraints.Required
    private String productId;
    @Constraints.Required
    private Integer variantId;
    @Constraints.Required
    @Constraints.Min(1)
    private Long quantity;

    public DefaultAddProductToCartFormData() {
    }

    @Override
    public String getProductId() {
        return productId;
    }

    public void setProductId(final String productId) {
        this.productId = productId;
    }

    @Override
    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(final Integer variantId) {
        this.variantId = variantId;
    }

    @Override
    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(final Long quantity) {
        this.quantity = quantity;
    }
}
