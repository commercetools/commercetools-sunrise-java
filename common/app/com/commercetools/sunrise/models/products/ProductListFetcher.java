package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultProductListFetcher.class)
public interface ProductListFetcher extends ResourceFetcher<PagedSearchResult<ProductProjection>, ProductProjectionSearch> {

    Optional<ProductProjectionSearch> defaultRequest();

    CompletionStage<PagedSearchResult<ProductProjection>> get();
}
