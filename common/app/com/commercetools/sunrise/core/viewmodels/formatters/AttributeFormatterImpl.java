package com.commercetools.sunrise.core.viewmodels.formatters;

import com.commercetools.sunrise.models.products.AttributeWithProductType;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.attributes.AttributeDefinition;
import io.sphere.sdk.products.attributes.DefaultProductAttributeFormatter;
import io.sphere.sdk.producttypes.ProductTypeLocalRepository;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.Collections.singletonList;

@Singleton
final class AttributeFormatterImpl implements AttributeFormatter {

    private static final Pattern ATTR_WHITELIST = Pattern.compile("[^0-9a-zA-Z]+");
    private final ProductTypeLocalRepository productTypeLocalRepository;
    private final Provider<Locale> localeProvider;

    @Inject
    AttributeFormatterImpl(final ProductTypeLocalRepository productTypeLocalRepository, final Provider<Locale> localeProvider) {
        this.productTypeLocalRepository = productTypeLocalRepository;
        this.localeProvider = localeProvider;
    }

    @Override
    public LocalizedString label(@Nullable final AttributeWithProductType nullableAttributeWithProductType) {
        return Optional.ofNullable(nullableAttributeWithProductType)
                .flatMap(attributeWithProductType -> {
                    final String productTypeId = attributeWithProductType.getProductTypeRef().toReference().getId();
                    return productTypeLocalRepository.findById(productTypeId)
                            .flatMap(productType -> productType.findAttribute(attributeWithProductType.getAttribute().getName()))
                            .map(AttributeDefinition::getLabel);
                }).orElseGet(LocalizedString::empty);
    }

    @Override
    public String value(@Nullable final AttributeWithProductType nullableAttributeWithProductType) {
        final DefaultProductAttributeFormatter formatter = new DefaultProductAttributeFormatter(productTypeLocalRepository, locales());
        return Optional.ofNullable(nullableAttributeWithProductType)
                .flatMap(attributeWithProductType -> Optional.ofNullable(formatter.convert(attributeWithProductType.getAttribute(), attributeWithProductType.getProductTypeRef())))
                .orElse("");
    }

    @Override
    public String encodedValue(@Nullable final AttributeWithProductType nullableAttributeWithProductType) {
        return Optional.ofNullable(nullableAttributeWithProductType)
                .map(attributeWithProductType -> ATTR_WHITELIST.matcher(value(attributeWithProductType)).replaceAll(""))
                .orElse("");
    }

    private List<Locale> locales() {
        return singletonList(localeProvider.get());
    }
}
