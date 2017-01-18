package com.commercetools.sunrise.common.utils;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.ctp.ProductDataConfig;
import io.sphere.sdk.models.EnumValue;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.products.attributes.AttributeAccess;
import io.sphere.sdk.products.attributes.AttributeExtraction;

import javax.inject.Inject;
import java.util.regex.Pattern;

@RequestScoped
final class AttributeFormatterImpl implements AttributeFormatter {

    private static final Pattern ATTR_WHITELIST = Pattern.compile("[^0-9a-zA-Z]+");
    private final LocalizedStringResolver localizedStringResolver;
    private final ProductDataConfig productDataConfig;

    @Inject
    AttributeFormatterImpl(final LocalizedStringResolver localizedStringResolver, final ProductDataConfig productDataConfig) {
        this.localizedStringResolver = localizedStringResolver;
        this.productDataConfig = productDataConfig;
    }

    @Override
    public String label(final Attribute attribute) {
        return productDataConfig.getMetaProductType()
                .findAttribute(attribute.getName())
                .map(def -> localizedStringResolver.getOrEmpty(def.getLabel()))
                .orElse("");
    }

    @Override
    public String value(final Attribute attribute) {
        final AttributeExtraction<String> attributeExtraction = AttributeExtraction.of(productDataConfig.getMetaProductType(), attribute);
        return attributeExtraction
                .ifIs(AttributeAccess.ofLocalizedString(), localizedStringResolver::getOrEmpty)
                .ifIs(AttributeAccess.ofLocalizedEnumValue(), v -> localizedStringResolver.getOrEmpty(v.getLabel()))
                .ifIs(AttributeAccess.ofEnumValue(), EnumValue::getLabel)
                .ifIs(AttributeAccess.ofString(), v -> v)
                .findValue()
                .orElse("");
    }

    @Override
    public String valueAsKey(final Attribute attribute) {
        return ATTR_WHITELIST.matcher(value(attribute)).replaceAll("");
    }
}
