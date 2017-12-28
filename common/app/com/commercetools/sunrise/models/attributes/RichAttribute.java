package com.commercetools.sunrise.models.attributes;

import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.products.attributes.AttributeAccess;
import play.libs.Json;

import javax.annotation.Nullable;

public class RichAttribute implements Attribute {

    private final Attribute attribute;
    private final ProductAttributeFormatter attributeFormatter;

    public RichAttribute(final Attribute attribute, final ProductAttributeFormatter attributeFormatter) {
        this.attribute = attribute;
        this.attributeFormatter = attributeFormatter;
    }

    @Override
    public String getName() {
        return attribute.getName();
    }

    @Override
    public <T> T getValue(final AttributeAccess<T> access) {
        return attribute.getValue(access);
    }

    @Nullable
    public String getLabel() {
        final String attributeName = attribute.getName();
        return attributeFormatter.label(attributeName).orElse(attributeName);
    }

    @Nullable
    public String getValue() {
        return attributeFormatter.convert(attribute)
                .orElseGet(() -> Json.stringify(attribute.getValueAsJsonNode()));
    }
}
