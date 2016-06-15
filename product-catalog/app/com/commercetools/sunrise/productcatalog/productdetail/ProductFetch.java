package com.commercetools.sunrise.productcatalog.productdetail;

import io.sphere.sdk.products.search.ProductProjectionSearch;

import java.util.concurrent.CompletionStage;
import java.util.function.UnaryOperator;

public interface ProductFetch<P, V> {

    CompletionStage<ProductFetchResult> findProduct(final P productIdentifier,
                                                    final V variantIdentifier,
                                                    final UnaryOperator<ProductProjectionSearch> runHookOnProductSearch);
}
