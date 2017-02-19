package com.commercetools.sunrise.productcatalog.productdetail;

import com.commercetools.sunrise.framework.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.products.ProductProjection;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@ImplementedBy(ProductFinderBySlug.class)
@FunctionalInterface
public interface ProductFinder extends ResourceFinder, Function<String, CompletionStage<Optional<ProductProjection>>> {

    @Override
    CompletionStage<Optional<ProductProjection>> apply(final String identifier);
}
