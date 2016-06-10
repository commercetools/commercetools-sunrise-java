package com.commercetools.sunrise.productcatalog.hooks;

import common.hooks.Hook;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

public interface ProductProjectionPagedSearchResultHook extends Hook {
    void acceptProductProjectionPagedSearchResult(final PagedSearchResult<ProductProjection> pagedSearchResult);
}
