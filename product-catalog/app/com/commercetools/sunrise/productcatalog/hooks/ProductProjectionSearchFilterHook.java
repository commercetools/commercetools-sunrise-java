package com.commercetools.sunrise.productcatalog.hooks;

import common.hooks.Hook;
import io.sphere.sdk.products.search.ProductProjectionSearch;

public interface ProductProjectionSearchFilterHook extends Hook {
    ProductProjectionSearch filterProductProjectionSearch(ProductProjectionSearch search);
}
