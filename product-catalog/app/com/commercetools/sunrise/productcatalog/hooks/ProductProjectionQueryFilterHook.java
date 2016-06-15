package com.commercetools.sunrise.productcatalog.hooks;

import com.commercetools.sunrise.hooks.Hook;
import io.sphere.sdk.products.queries.ProductProjectionQuery;

public interface ProductProjectionQueryFilterHook extends Hook {
    ProductProjectionQuery filterProductProjectionQuery(ProductProjectionQuery query);
}
