package productcatalog.controllers;

import common.controllers.PageContent;
import productcatalog.models.SuggestionsData;

public class HomePageContent extends PageContent {
    private SuggestionsData suggestions;

    public SuggestionsData getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final SuggestionsData suggestions) {
        this.suggestions = suggestions;
    }
}
