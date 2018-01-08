package com.commercetools.sunrise.core.controllers;

import com.commercetools.sunrise.models.products.ProductWithVariant;
import io.sphere.sdk.client.NotFoundException;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public interface ResourceFetcher<T> {

    default CompletionStage<ProductWithVariant> require(final CompletionStage<Optional<ProductWithVariant>> resourceStage) {
        return resourceStage.thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundException::new));
    }
}
