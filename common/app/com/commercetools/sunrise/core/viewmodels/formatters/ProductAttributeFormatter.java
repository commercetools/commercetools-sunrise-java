package com.commercetools.sunrise.core.viewmodels.formatters;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.models.Referenceable;
import io.sphere.sdk.products.attributes.Attribute;
import io.sphere.sdk.products.attributes.ProductAttributeConverter;
import io.sphere.sdk.producttypes.ProductType;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.regex.Pattern;

@ImplementedBy(ProductAttributeFormatterImpl.class)
public interface ProductAttributeFormatter extends ProductAttributeConverter<Optional<String>> {

    Pattern ATTR_WHITELIST = Pattern.compile("[^0-9a-zA-Z_\\-+]+");

    Optional<String> label(String attributeName, Referenceable<ProductType> productType);

    @Nonnull
    @Override
    Optional<String> convert(Attribute attribute, Referenceable<ProductType> productType);

    default Optional<String> convertEncoded(Attribute attribute, Referenceable<ProductType> productType) {
        return convert(attribute, productType)
                .map(unescapedValue -> ATTR_WHITELIST.matcher(unescapedValue).replaceAll(""));
    }
}
