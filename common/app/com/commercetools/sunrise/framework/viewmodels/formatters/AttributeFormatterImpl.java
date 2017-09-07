package com.commercetools.sunrise.framework.viewmodels.formatters;

import com.commercetools.sunrise.framework.injection.RequestScoped;
import com.commercetools.sunrise.framework.viewmodels.content.products.AttributeWithProductType;
import io.sphere.sdk.models.LocalizedString;
import io.sphere.sdk.products.attributes.AttributeDefinition;
import io.sphere.sdk.products.attributes.DefaultProductAttributeFormatter;
import io.sphere.sdk.products.attributes.ProductAttributeConverter;
import io.sphere.sdk.producttypes.ProductTypeLocalRepository;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

import static java.util.Collections.singletonList;

@RequestScoped
final class AttributeFormatterImpl implements AttributeFormatter {

    private static final Pattern ATTR_WHITELIST = Pattern.compile("[^0-9a-zA-Z]+");
    private final ProductAttributeConverter<String> formatter;
    private final ProductTypeLocalRepository productTypeLocalRepository;

    @Inject
    AttributeFormatterImpl(final ProductTypeLocalRepository productTypeLocalRepository, final Locale locale) {
        this.formatter = new DefaultProductAttributeFormatter(productTypeLocalRepository, singletonList(locale));
        this.productTypeLocalRepository = productTypeLocalRepository;
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
}
