package com.commercetools.sunrise.hooks;

import io.sphere.sdk.products.search.ProductProjectionSearch;

public interface ProductProjectionSearchFilterHook extends QueryFilterHook<ProductProjectionSearch> {

    @Override
    ProductProjectionSearch filterQuery(ProductProjectionSearch search);
}
