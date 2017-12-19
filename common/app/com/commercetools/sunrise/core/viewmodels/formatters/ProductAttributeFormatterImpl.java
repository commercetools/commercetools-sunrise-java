package com.commercetools.sunrise.core.viewmodels.formatters;

import com.commercetools.sunrise.core.i18n.I18nResolver;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.products.attributes.AttributeDefinition;
import io.sphere.sdk.products.attributes.DefaultProductAttributeFormatter;
import io.sphere.sdk.producttypes.ProductType;
import io.sphere.sdk.producttypes.ProductTypeLocalRepository;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

import static java.util.Collections.singletonList;

@Singleton
final class ProductAttributeFormatterImpl implements ProductAttributeFormatter {

    private final ProductTypeLocalRepository productTypeLocalRepository;
    private final I18nResolver i18nResolver;

    @Inject
    ProductAttributeFormatterImpl(final ProductTypeLocalRepository productTypeLocalRepository, final I18nResolver i18nResolver) {
        this.productTypeLocalRepository = productTypeLocalRepository;
        this.i18nResolver = i18nResolver;
    }

    @Override
    public Optional<String> label(final String attributeName, final Referenceable<ProductType> productTypeRef) {
        return productTypeLocalRepository.findById(productTypeRef.toReference().getId())
                .flatMap(productType -> productType.findAttribute(attributeName))
                .map(AttributeDefinition::getLabel)
                .flatMap(i18nResolver::get);
    }

    @Nonnull
    @Override
    public Optional<String> convert(final Attribute attribute, final Referenceable<ProductType> productType) {
        return Optional.ofNullable(formatter().convert(attribute, productType));
    }

    private DefaultProductAttributeFormatter formatter() {
        return new DefaultProductAttributeFormatter(productTypeLocalRepository, singletonList(i18nResolver.currentLanguage()));
    }
}
