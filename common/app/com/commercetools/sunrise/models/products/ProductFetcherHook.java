package com.commercetools.sunrise.models.products;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.products.queries.ProductProjectionQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface ProductFetcherHook extends FilterHook {

    CompletionStage<Optional<ProductWithVariant>> on(ProductProjectionQuery request, Function<ProductProjectionQuery, CompletionStage<Optional<ProductWithVariant>>> nextComponent);
}
