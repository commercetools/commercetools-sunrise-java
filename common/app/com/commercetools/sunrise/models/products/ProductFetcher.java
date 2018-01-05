package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.commercetools.sunrise.models.products.DefaultProductFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.products.ProductProjection;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(DefaultProductFetcher.class)
@FunctionalInterface
public interface ProductFetcher extends ResourceFetcher<ProductProjection>, Function<String, CompletionStage<Optional<ProductProjection>>> {

    @Override
    CompletionStage<Optional<ProductProjection>> apply(final String identifier);
}
