package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultProductListFetcher.class)
@FunctionalInterface
public interface ProductListFetcher extends ResourceFetcher<ProductProjection> {

    CompletionStage<PagedSearchResult<ProductProjection>> get();
}
