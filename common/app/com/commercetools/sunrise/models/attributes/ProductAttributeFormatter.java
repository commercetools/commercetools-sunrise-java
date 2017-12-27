package com.commercetools.sunrise.models.attributes;

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

    @Nonnull
    @Override
    Optional<String> convert(Attribute attribute, Referenceable<ProductType> productType);

    default Optional<String> convert(Attribute attribute) {
        return productTypeOf(attribute.getName())
                .flatMap(productType -> convert(attribute, productType));
    }

    Optional<String> label(String attributeName, Referenceable<ProductType> productType);

    default Optional<String> label(String attributeName) {
        return productTypeOf(attributeName)
                .flatMap(productType -> label(attributeName, productType));
    }

    Optional<ProductType> productTypeOf(String attributeName);
}
