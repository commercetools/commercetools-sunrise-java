package productcatalog.controllers;

import common.controllers.PageContent;
import productcatalog.models.SuggestionsData;

public class HomePageContent extends PageContent {
    private String additionalTitle;
    private SuggestionsData suggestions;

    @Override
    public String getAdditionalTitle() {
        return additionalTitle;
    }

    public void setAdditionalTitle(final String additionalTitle) {
        this.additionalTitle = additionalTitle;
    }

    public SuggestionsData getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final SuggestionsData suggestions) {
        this.suggestions = suggestions;
    }
}
