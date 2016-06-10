package com.commercetools.sunrise.productcatalog.productoverview.search.searchbox;

import com.commercetools.sunrise.common.controllers.SunrisePageData;
import com.commercetools.sunrise.common.hooks.SunrisePageDataHook;
import framework.ControllerComponent;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import com.commercetools.sunrise.productcatalog.hooks.ProductProjectionSearchFilterHook;
import com.commercetools.sunrise.productcatalog.productoverview.ProductOverviewPageContent;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class SearchBoxComponent implements ControllerComponent, SunrisePageDataHook, ProductProjectionSearchFilterHook {

    @Nullable
    private SearchBox searchBox;

    @Inject
    private SearchBoxFactory searchBoxFactory;

    @Override
    public ProductProjectionSearch filterProductProjectionSearch(final ProductProjectionSearch search) {
        this.searchBox = searchBoxFactory.create();
        return searchBox.getSearchTerm()
                .map(search::withText)
                .orElse(search);
    }

    @Override
    public void acceptSunrisePageData(final SunrisePageData sunrisePageData) {
        if (searchBox != null && searchBox.getSearchTerm().isPresent() && sunrisePageData.getContent() instanceof ProductOverviewPageContent) {
            final ProductOverviewPageContent content = (ProductOverviewPageContent) sunrisePageData.getContent();
            final String searchTerm = searchBox.getSearchTerm().get().getValue();
            content.setSearchTerm(searchTerm);
        }
    }
}
