package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.models.SelectOption;
import io.sphere.sdk.products.ProductVariant;

public class AttributeSelectOption extends SelectOption {

    private ProductVariant variant;
    private Boolean secondary;

    public ProductVariant getVariant() {
        return variant;
    }

    public void setVariant(final ProductVariant variant) {
        this.variant = variant;
    }

    public Boolean getSecondary() {
        return secondary;
    }

    public void setSecondary(final Boolean secondary) {
        this.secondary = secondary;
    }
}
