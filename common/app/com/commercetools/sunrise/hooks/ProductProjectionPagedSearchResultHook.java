package com.commercetools.sunrise.hooks;

import com.commercetools.sunrise.hooks.Hook;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

public interface ProductProjectionPagedSearchResultHook extends Hook {
    void acceptProductProjectionPagedSearchResult(final PagedSearchResult<ProductProjection> pagedSearchResult);
}
