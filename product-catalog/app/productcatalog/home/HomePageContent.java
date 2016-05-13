package productcatalog.home;

import common.controllers.PageContent;
import productcatalog.common.SuggestionsData;

public class HomePageContent extends PageContent {

    private SuggestionsData suggestions;

    public HomePageContent() {
    }

    public SuggestionsData getSuggestions() {
        return suggestions;
    }

    public void setSuggestions(final SuggestionsData suggestions) {
        this.suggestions = suggestions;
    }
}
