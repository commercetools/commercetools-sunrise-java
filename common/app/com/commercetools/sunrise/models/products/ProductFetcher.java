package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.queries.ProductProjectionQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultProductFetcher.class)
public interface ProductFetcher extends ResourceFetcher<ProductProjection, ProductProjectionQuery, PagedQueryResult<ProductProjection>> {

    Optional<ProductProjectionQuery> defaultRequest(final String productIdentifier);

    CompletionStage<Optional<ProductWithVariant>> get(final String productIdentifier, @Nullable final String variantIdentifier);

    default CompletionStage<ProductWithVariant> require(final String productIdentifier, @Nullable final String variantIdentifier) {
        return get(productIdentifier, variantIdentifier).thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
