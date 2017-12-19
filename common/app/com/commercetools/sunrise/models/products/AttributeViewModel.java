package com.commercetools.sunrise.models.products;

import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.products.attributes.AttributeAccess;

public class AttributeViewModel implements Attribute {

    private final Attribute attribute;
    private final LocalizedString label;
    private final String value;
    private final boolean reloaded;
    private final boolean selected;

    public AttributeViewModel(final Attribute attribute, final LocalizedString label, final String value,
                              final boolean reloaded, final boolean selected) {
        this.attribute = attribute;
        this.label = label;
        this.value = value;
        this.reloaded = reloaded;
        this.selected = selected;
    }

    @Override
    public String getName() {
        return attribute.getName();
    }

    @Override
    public <T> T getValue(final AttributeAccess<T> access) {
        return attribute.getValue(access);
    }

    public LocalizedString getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }

    public boolean isReloaded() {
        return reloaded;
    }

    public boolean isSelected() {
        return selected;
    }
}
