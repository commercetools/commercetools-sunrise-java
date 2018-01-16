package com.commercetools.sunrise.models.attributes;

import com.commercetools.sunrise.models.products.AttributeSelectOption;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;
import play.libs.Json;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

@Singleton
public class AttributeHelperSource {

    private final AttributeSelector attributeSelector;
    private final AttributeSettings attributesSettings;
    private final ProductAttributeFormatter attributeFormatter;

    @Inject
    AttributeHelperSource(final AttributeSelector attributeSelector, final AttributeSettings attributesSettings,
                          final ProductAttributeFormatter attributeFormatter) {
        this.attributeSelector = attributeSelector;
        this.attributesSettings = attributesSettings;
        this.attributeFormatter = attributeFormatter;
    }

    public CharSequence attributeLabel(final Attribute attribute) {
        final String attributeName = attribute.getName();
        return attributeFormatter.label(attributeName).orElse(attributeName);
    }

    public CharSequence attributeValue(final Attribute attribute) {
        return attributeFormatter.convert(attribute).orElseGet(() -> Json.stringify(attribute.getValueAsJsonNode()));
    }

    public CharSequence withAttributeOptions(final Attribute attribute, final ProductProjection product,
                                             final ProductVariant variant, final Options options) throws IOException {
        final List<AttributeSelectOption> attributeOptions = attributeSelector.options(attribute.getName(), product, variant);
        return attributeOptions.isEmpty() ? null : options.fn(attributeOptions);
    }

    public CharSequence withDisplayedAttributes(final ProductVariant variant, final Options options) throws IOException {
        return withAttributes(attributesSettings.displayed(), variant, options);
    }

    public CharSequence withSelectableAttributes(final ProductVariant variant, final Options options) throws IOException {
        return withAttributes(attributesSettings.selectable(), variant, options);
    }

    private CharSequence withAttributes(final List<String> attributeNames, final ProductVariant variant, final Options options) throws IOException {
        final List<Attribute> attributes = attributeNames.stream()
                .map(variant::getAttribute)
                .filter(Objects::nonNull)
                .collect(toList());
        return attributes.isEmpty() ? null : options.fn(attributes);
    }
}
