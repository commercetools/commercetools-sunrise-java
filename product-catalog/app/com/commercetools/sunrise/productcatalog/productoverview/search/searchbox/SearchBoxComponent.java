package com.commercetools.sunrise.productcatalog.productoverview.search.searchbox;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.hooks.PageDataHook;
import com.commercetools.sunrise.framework.ControllerComponent;
import io.sphere.sdk.products.search.ProductProjectionSearch;
import com.commercetools.sunrise.hooks.ProductProjectionSearchFilterHook;
import com.commercetools.sunrise.productcatalog.productoverview.ProductOverviewPageContent;

import javax.annotation.Nullable;
import javax.inject.Inject;

public class SearchBoxComponent implements ControllerComponent, PageDataHook, ProductProjectionSearchFilterHook {

    @Nullable
    private SearchBox searchBox;

    @Inject
    private SearchBoxFactory searchBoxFactory;

    @Override
    public ProductProjectionSearch filterQuery(final ProductProjectionSearch search) {
        this.searchBox = searchBoxFactory.create();
        return searchBox.getSearchTerm()
                .map(search::withText)
                .orElse(search);
    }

    @Override
    public void acceptPageData(final PageData pageData) {
        if (searchBox != null && searchBox.getSearchTerm().isPresent() && pageData.getContent() instanceof ProductOverviewPageContent) {
            final ProductOverviewPageContent content = (ProductOverviewPageContent) pageData.getContent();
            final String searchTerm = searchBox.getSearchTerm().get().getValue();
            content.setSearchTerm(searchTerm);
        }
    }
}
