package com.commercetools.sunrise.productcatalog.home;

import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.productcatalog.common.SuggestionsBean;

public class HomePageContent extends PageContent {

    private SuggestionsBean suggestions;

    public HomePageContent() {
    }

    public SuggestionsBean getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final SuggestionsBean suggestions) {
        this.suggestions = suggestions;
    }
}
