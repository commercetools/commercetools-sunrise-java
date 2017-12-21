package com.commercetools.sunrise.models.attributes;

import com.commercetools.sunrise.core.viewmodels.formatters.ProductAttributeFormatter;
import com.github.jknack.handlebars.Options;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeLocalRepository;
import play.libs.Json;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Singleton
public class AttributeHelperSource {

    private final ProductAttributeFormatter productAttributeFormatter;
    private final AttributeOptionFactory attributeOptionFactory;
    private final ProductAttributesSettings attributesSettings;
    private final ProductTypeLocalRepository productTypeLocalRepository;

    @Inject
    AttributeHelperSource(final ProductAttributeFormatter productAttributeFormatter, final AttributeOptionFactory attributeOptionFactory,
                          final ProductAttributesSettings attributesSettings, final ProductTypeLocalRepository productTypeLocalRepository) {
        this.productAttributeFormatter = productAttributeFormatter;
        this.attributeOptionFactory = attributeOptionFactory;
        this.attributesSettings = attributesSettings;
        this.productTypeLocalRepository = productTypeLocalRepository;
    }

    public CharSequence withDisplayedAttributes(final ProductVariant variant, final Options options) throws IOException {
        return withAttributes(attributesSettings.displayed(), variant, options);
    }

    public CharSequence withPrimarySelectableAttributes(final ProductVariant variant, final Options options) throws IOException {
        return withAttributes(attributesSettings.primarySelectable(), variant, options);
    }

    public CharSequence withSecondarySelectableAttributes(final ProductVariant variant, final Options options) throws IOException {
        return withAttributes(attributesSettings.secondarySelectable(), variant, options);
    }

    public CharSequence withAttributeOptions(final Attribute attribute, final ProductProjection product,
                                             final ProductVariant variant, final Options options) throws IOException {
        final List<AttributeOption> attributeOptions = attributeOptions(attribute.getName(), product, variant);
        return attributeOptions.isEmpty() ? null : options.fn(attributeOptions);
    }

    public CharSequence attributeLabel(final Attribute attribute, @Nullable final Reference<ProductType> nullableProductTypeRef) {
        final String attributeName = attribute.getName();
        return productTypeOrDefault(attributeName, nullableProductTypeRef)
                .flatMap(productType -> productAttributeFormatter.label(attributeName, productType))
                .orElse(attributeName);
    }

    public CharSequence attributeValue(final Attribute attribute, @Nullable final Reference<ProductType> nullableProductTypeRef) {
        return productTypeOrDefault(attribute.getName(), nullableProductTypeRef)
                .flatMap(productTypeRef -> productAttributeFormatter.convert(attribute, productTypeRef))
                .orElseGet(() -> Json.stringify(attribute.getValueAsJsonNode()));
    }

    private List<AttributeOption> attributeOptions(final String attributeName, final ProductProjection product,
                                                   final ProductVariant currentVariant) {
        return product.getAllVariants().stream()
                .map(variant -> variant.getAttribute(attributeName))
                .filter(Objects::nonNull)
                .distinct()
                .map(attribute -> attributeOptionFactory.create(attribute, product, currentVariant))
                .collect(toList());
    }

    private Optional<ProductType> productTypeOrDefault(final String attributeName,
                                                       @Nullable final Reference<ProductType> nullableProductTypeRef) {
        return Optional.ofNullable(nullableProductTypeRef)
                .flatMap(productTypeRef -> productTypeLocalRepository.findById(productTypeRef.getId()))
                .map(Optional::of)
                .orElseGet(() -> productTypeLocalRepository.getAll().stream()
                        .filter(productType -> productType.findAttribute(attributeName).isPresent())
                        .findFirst());
    }

    private CharSequence withAttributes(final List<String> attributeNames, final ProductVariant variant, final Options options) throws IOException {
        final List<Attribute> attributes = attributeNames.stream()
                .map(variant::getAttribute)
                .filter(Objects::nonNull)
                .collect(toList());
        return attributes.isEmpty() ? null : options.fn(attributes);
    }
}
