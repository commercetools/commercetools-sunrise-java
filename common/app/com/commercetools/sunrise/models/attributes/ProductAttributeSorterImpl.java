package com.commercetools.sunrise.models.attributes;

import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.models.WithKey;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.products.attributes.EnumAttributeType;
import io.sphere.sdk.products.attributes.LocalizedEnumAttributeType;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeLocalRepository;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

@Singleton
final class ProductAttributeSorterImpl implements ProductAttributeSorter {

    private final ProductTypeLocalRepository productTypeLocalRepository;
    private final Provider<Locale> localeProvider;

    @Inject
    ProductAttributeSorterImpl(final ProductTypeLocalRepository productTypeLocalRepository, final Provider<Locale> localeProvider) {
        this.productTypeLocalRepository = productTypeLocalRepository;
        this.localeProvider = localeProvider;
    }

    @Override
    public Comparator<Attribute> compare(final String attributeName, final Referenceable<ProductType> productTypeRef) {
        return productTypeLocalRepository.findById(productTypeRef.toReference().getId())
                .flatMap(productType -> productType.findAttribute(attributeName))
                .flatMap(attributeDefinition -> AttributeTypeExtractor.<Comparator<Attribute>>of(attributeDefinition)
                        .ifIsEnum(this::enumComparator)
                        .ifIsLocalizedEnum(this::localizedEnumComparator)
                        .ifIsLocalizedString(x -> localizedStringComparator())
                        .ifIsString(x -> comparing(Attribute::getValueAsString))
                        .ifIsNumber(x -> comparing(Attribute::getValueAsDouble))
                        .ifIsMoney(x -> comparing(Attribute::getValueAsMoney))
                        .ifIsDate(x -> comparing(Attribute::getValueAsLocalDate))
                        .ifIsTime(x -> comparing(Attribute::getValueAsLocalTime))
                        .ifIsDateTime(x -> comparing(Attribute::getValueAsDateTime))
                        .get())
                .orElseGet(this::neutralComparator);
    }

    private Comparator<Attribute> localizedStringComparator() {
        final Locale locale = localeProvider.get();
        return (a1, a2) -> {
            final Optional<String> loc1 = a1.getValueAsLocalizedString().find(locale);
            final Optional<String> loc2 = a2.getValueAsLocalizedString().find(locale);
            return loc1.isPresent() && loc2.isPresent() ? loc1.get().compareTo(loc2.get()) : 0;
        };
    }

    private Comparator<Attribute> localizedEnumComparator(final LocalizedEnumAttributeType attributeType) {
        final List<String> enumKeys = convertToKeys(attributeType.getValues());
        return (a1, a2) -> comparePositions(a1.getValueAsLocalizedEnumValue().getKey(), a2.getValueAsLocalizedEnumValue().getKey(), enumKeys);
    }

    private Comparator<Attribute> enumComparator(final EnumAttributeType attributeType) {
        final List<String> enumKeys = convertToKeys(attributeType.getValues());
        return (a1, a2) -> comparePositions(a1.getValueAsEnumValue().getKey(), a2.getValueAsEnumValue().getKey(), enumKeys);
    }

    private List<String> convertToKeys(final List<? extends WithKey> typeWithKeyList) {
        return typeWithKeyList.stream()
                .map(WithKey::getKey)
                .collect(toList());
    }

    private Comparator<Attribute> neutralComparator() {
        return (a1, a2) -> 0;
    }

    private static <T> int comparePositions(final T left, final T right, final List<T> customSortedValues) {
        final int leftPosition = customSortedValues.indexOf(left);
        final int rightPosition = customSortedValues.indexOf(right);
        return comparePositions(leftPosition, rightPosition);
    }

    static int comparePositions(final int leftPosition, final int rightPosition) {
        final int comparison;
        if (leftPosition == rightPosition) {
            comparison = 0;
        } else if (leftPosition < 0) {
            comparison = 1;
        } else if (rightPosition < 0) {
            comparison = -1;
        } else {
            comparison = Integer.compare(leftPosition, rightPosition);
        }
        return comparison;
    }
}
