package com.commercetools.sunrise.productcatalog.productoverview;

import io.sphere.sdk.categories.Category;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

import javax.annotation.Nullable;

public final class ProductOverviewPageData extends Base {

    public final PagedSearchResult<ProductProjection> searchResult;
    @Nullable
    public final Category category;

    public ProductOverviewPageData(final PagedSearchResult<ProductProjection> searchResult, @Nullable final Category category) {
        this.searchResult = searchResult;
        this.category = category;
    }
}
