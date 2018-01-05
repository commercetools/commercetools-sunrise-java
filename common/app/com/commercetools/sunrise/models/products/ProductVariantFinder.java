package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.Optional;
import java.util.function.BiFunction;

@ImplementedBy(DefaultProductVariantFinder.class)
@FunctionalInterface
public interface ProductVariantFinder extends ResourceFinder<ProductVariant>, BiFunction<ProductProjection, String, Optional<ProductVariant>> {

    @Override
    Optional<ProductVariant> apply(final ProductProjection product, final String identifier);
}
