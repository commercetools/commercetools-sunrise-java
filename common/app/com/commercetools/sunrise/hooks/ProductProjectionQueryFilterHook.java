package com.commercetools.sunrise.hooks;

import io.sphere.sdk.products.queries.ProductProjectionQuery;

public interface ProductProjectionQueryFilterHook extends QueryFilterHook<ProductProjectionQuery> {

    @Override
    ProductProjectionQuery filterQuery(ProductProjectionQuery query);
}
