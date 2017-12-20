package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.viewmodels.ViewModel;
import io.sphere.sdk.products.attributes.Attribute;

public class AttributeOption extends ViewModel {

    private final Attribute attribute;
    private String variantUrl;
    private Integer variantId;
    private boolean selected;
    private boolean disabled;

    public AttributeOption(final Attribute attribute) {
        this.attribute = attribute;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public String getVariantUrl() {
        return variantUrl;
    }

    public void setVariantUrl(final String variantUrl) {
        this.variantUrl = variantUrl;
    }

    public Integer getVariantId() {
        return variantId;
    }

    public void setVariantId(final Integer variantId) {
        this.variantId = variantId;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(final boolean selected) {
        this.selected = selected;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(final boolean disabled) {
        this.disabled = disabled;
    }
}
