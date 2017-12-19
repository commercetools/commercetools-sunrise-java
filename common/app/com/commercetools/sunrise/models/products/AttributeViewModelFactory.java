package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.viewmodels.formatters.AttributeFormatter;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AttributeViewModelFactory {

    private final AttributeFormatter attributeFormatter;
    private final ProductAttributesSettings attributesSettings;

    @Inject
    protected AttributeViewModelFactory(final AttributeFormatter attributeFormatter, final ProductAttributesSettings attributesSettings) {
        this.attributeFormatter = attributeFormatter;
        this.attributesSettings = attributesSettings;
    }

    public AttributeViewModel create(final Attribute attribute, final Reference<ProductType> productTypeRef,
                                     @Nullable final Attribute selectedAttribute) {
        final LocalizedString label = attributeFormatter.label(attribute, productTypeRef);
        final String value = attributeFormatter.value(attribute, productTypeRef);
        final boolean reloaded = attributesSettings.primarySelectable().contains(attribute.getName());
        final boolean selected = selected(attribute, selectedAttribute);
        return new AttributeViewModel(attribute, label, value, reloaded, selected);
    }

    public boolean selected(final Attribute attribute, @Nullable final Attribute selectedAttribute) {
        return selectedAttribute != null && attribute.getName().equals(selectedAttribute.getName())
                && attribute.getValueAsJsonNode().equals(selectedAttribute.getValueAsJsonNode());
    }
}
