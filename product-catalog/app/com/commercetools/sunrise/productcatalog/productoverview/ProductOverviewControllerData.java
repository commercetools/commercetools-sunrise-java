package com.commercetools.sunrise.productcatalog.productoverview;

import com.commercetools.sunrise.common.models.ControllerData;
import io.sphere.sdk.categories.Category;
import io.sphere.sdk.models.Base;
import io.sphere.sdk.products.ProductProjection;
import io.sphere.sdk.search.PagedSearchResult;

import javax.annotation.Nullable;

public final class ProductOverviewControllerData extends ControllerData {

    private final PagedSearchResult<ProductProjection> searchResult;
    @Nullable
    private final Category category;

    public ProductOverviewControllerData(final PagedSearchResult<ProductProjection> searchResult, @Nullable final Category category) {
        this.searchResult = searchResult;
        this.category = category;
    }

    public PagedSearchResult<ProductProjection> getSearchResult() {
        return searchResult;
    }

    @Nullable
    public Category getCategory() {
        return category;
    }
}
