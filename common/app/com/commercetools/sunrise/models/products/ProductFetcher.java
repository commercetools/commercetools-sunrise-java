package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.products.ProductProjection;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultProductFetcher.class)
@FunctionalInterface
public interface ProductFetcher extends ResourceFetcher<ProductProjection> {

    CompletionStage<Optional<ProductWithVariant>> get(final String productIdentifier, final String variantIdentifier);

    default CompletionStage<ProductWithVariant> require(final String productIdentifier, final String variantIdentifier) {
        return require(get(productIdentifier, variantIdentifier));
    }
}
