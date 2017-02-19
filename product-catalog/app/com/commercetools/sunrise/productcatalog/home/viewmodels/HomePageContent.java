package com.commercetools.sunrise.productcatalog.home.viewmodels;

import com.commercetools.sunrise.productcatalog.productoverview.viewmodels.ProductListBean;
import com.commercetools.sunrise.common.pages.PageContent;

public class HomePageContent extends PageContent {

    private ProductListBean suggestions;

    public HomePageContent() {
    }

    public ProductListBean getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final ProductListBean suggestions) {
        this.suggestions = suggestions;
    }
}
