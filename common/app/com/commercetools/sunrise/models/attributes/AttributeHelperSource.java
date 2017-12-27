package com.commercetools.sunrise.models.attributes;

import com.commercetools.sunrise.models.SelectOption;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Singleton
public class AttributeHelperSource {

    private final AttributeSelectOptionFactory attributeSelectOptionFactory;
    private final AttributeSettings attributesSettings;

    @Inject
    AttributeHelperSource(final AttributeSelectOptionFactory attributeSelectOptionFactory,
                          final AttributeSettings attributesSettings) {
        this.attributeSelectOptionFactory = attributeSelectOptionFactory;
        this.attributesSettings = attributesSettings;
    }

    public CharSequence withDisplayedAttributes(final ProductVariant variant, final Options options) throws IOException {
        return withAttributes(attributesSettings.displayed(), variant, options);
    }

    public CharSequence withSelectableAttributes(final ProductVariant variant, final Options options) throws IOException {
        return withAttributes(attributesSettings.selectable(), variant, options);
    }

    public CharSequence withAttributeOptions(final Attribute attribute, final ProductProjection product,
                                             final ProductVariant variant, final Options options) throws IOException {
        final List<SelectOption> attributeOptions = attributeSelectOptionFactory.createList(attribute.getName(), product, variant);
        return attributeOptions.isEmpty() ? null : options.fn(attributeOptions);
    }

    private CharSequence withAttributes(final List<String> attributeNames, final ProductVariant variant, final Options options) throws IOException {
        final List<Attribute> attributes = attributeNames.stream()
                .map(variant::getAttribute)
                .filter(Objects::nonNull)
                .collect(toList());
        return attributes.isEmpty() ? null : options.fn(attributes);
    }
}
