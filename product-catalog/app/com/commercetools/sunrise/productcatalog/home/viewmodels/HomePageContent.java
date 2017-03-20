package com.commercetools.sunrise.productcatalog.home.viewmodels;

import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductListViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;

public class HomePageContent extends PageContent {

    private ProductListViewModel suggestions;

    public HomePageContent() {
    }

    public ProductListViewModel getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final ProductListViewModel suggestions) {
        this.suggestions = suggestions;
    }
}
