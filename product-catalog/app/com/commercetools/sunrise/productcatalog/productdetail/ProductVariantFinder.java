package com.commercetools.sunrise.productcatalog.productdetail;

import com.google.inject.ImplementedBy;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.ProductVariant;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(ProductVariantFinderBySlug.class)
@FunctionalInterface
public interface ProductVariantFinder extends BiFunction<ProductProjection, String, CompletionStage<Optional<ProductVariant>>> {

    @Override
    CompletionStage<Optional<ProductVariant>> apply(final ProductProjection product, final String identifier);
}
