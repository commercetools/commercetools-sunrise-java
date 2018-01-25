package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.ResourceFetcher;
import com.google.inject.ImplementedBy;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultProductFetcher.class)
public interface ProductFetcher extends ResourceFetcher {

    CompletionStage<Optional<ProductWithVariant>> get(final String productIdentifier, @Nullable final String variantIdentifier);

    default CompletionStage<ProductWithVariant> require(final String productIdentifier, @Nullable final String variantIdentifier) {
        return get(productIdentifier, variantIdentifier).thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
