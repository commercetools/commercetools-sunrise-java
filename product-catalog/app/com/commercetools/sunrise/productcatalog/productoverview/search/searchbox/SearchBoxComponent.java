package com.commercetools.sunrise.productcatalog.productoverview.search.searchbox;

import com.commercetools.sunrise.common.pages.PageData;
import com.commercetools.sunrise.framework.ControllerComponent;
import com.commercetools.sunrise.hooks.consumers.PageDataReadyHook;
import com.commercetools.sunrise.hooks.requests.ProductProjectionSearchHook;
import com.commercetools.sunrise.productcatalog.productoverview.ProductOverviewPageContent;
import io.sphere.sdk.products.search.ProductProjectionSearch;

import javax.annotation.Nullable;
import javax.inject.Inject;

public final class SearchBoxComponent implements ControllerComponent, PageDataReadyHook, ProductProjectionSearchHook {

    private final SearchBoxFactory searchBoxFactory;

    @Nullable
    private SearchBox searchBox;

    @Inject
    public SearchBoxComponent(final SearchBoxFactory searchBoxFactory) {
        this.searchBoxFactory = searchBoxFactory;
    }

    @Override
    public ProductProjectionSearch onProductProjectionSearch(final ProductProjectionSearch search) {
        this.searchBox = searchBoxFactory.create();
        return searchBox.getSearchTerm()
                .map(search::withText)
                .orElse(search);
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        if (searchBox != null && searchBox.getSearchTerm().isPresent() && pageData.getContent() instanceof ProductOverviewPageContent) {
            final ProductOverviewPageContent content = (ProductOverviewPageContent) pageData.getContent();
            final String searchTerm = searchBox.getSearchTerm().get().getValue();
            content.setSearchTerm(searchTerm);
        }
    }
}
