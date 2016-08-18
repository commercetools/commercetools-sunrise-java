package com.commercetools.sunrise.productcatalog.productdetail;

import java.util.concurrent.CompletionStage;

public interface ProductFinder<P, V> {

    CompletionStage<ProductFinderResult> findProduct(final P productIdentifier,
                                                     final V variantIdentifier);
}
