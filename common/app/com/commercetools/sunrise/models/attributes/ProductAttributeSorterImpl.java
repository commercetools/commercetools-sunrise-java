package com.commercetools.sunrise.models.attributes;

import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.models.WithKey;
import io.sphere.sdk.products.attributes.*;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeLocalRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.List;

import static com.commercetools.sunrise.utils.SortUtils.comparePositions;
import static java.util.stream.Collectors.toList;

@Singleton
final class ProductAttributeSorterImpl implements ProductAttributeSorter {

    private final ProductTypeLocalRepository productTypeLocalRepository;

    @Inject
    ProductAttributeSorterImpl(final ProductTypeLocalRepository productTypeLocalRepository) {
        this.productTypeLocalRepository = productTypeLocalRepository;
    }

    @Override
    public Comparator<Attribute> comparator(final String attributeName, final Referenceable<ProductType> productTypeRef) {
        return productTypeLocalRepository.findById(productTypeRef.toReference().getId())
                .flatMap(productType -> productType.findAttribute(attributeName))
                .flatMap(attributeDefinition -> AttributeTypeExtractor.<Comparator<Attribute>>of(attributeDefinition)
                        .ifIsEnum(this::comparator)
                        .ifIsLocalizedEnum(this::comparator)
                        .ifIsString(this::comparator)
                        .ifIsNumber(this::comparator)
                        .get())
                .orElseGet(() -> (a1, a2) -> 0);
    }

    private Comparator<Attribute> comparator(final NumberAttributeType attributeType) {
        return Comparator.comparing(Attribute::getValueAsDouble);
    }

    private Comparator<Attribute> comparator(final StringAttributeType attributeType) {
        return Comparator.comparing(Attribute::getValueAsString);
    }

    private Comparator<Attribute> comparator(final LocalizedEnumAttributeType attributeType) {
        final List<String> enumKeys = convertToKeys(attributeType.getValues());
        return (attr1, attr2) -> comparePositions(attr1.getValueAsLocalizedEnumValue().getKey(), attr2.getValueAsLocalizedEnumValue().getKey(), enumKeys);
    }

    private Comparator<Attribute> comparator(final EnumAttributeType attributeType) {
        final List<String> enumKeys = convertToKeys(attributeType.getValues());
        return (attr1, attr2) -> comparePositions(attr1.getValueAsEnumValue().getKey(), attr2.getValueAsEnumValue().getKey(), enumKeys);
    }

    private List<String> convertToKeys(final List<? extends WithKey> typeWithKeyList) {
        return typeWithKeyList.stream()
                .map(WithKey::getKey)
                .collect(toList());
    }
}
