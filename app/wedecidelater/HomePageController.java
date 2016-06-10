package wedecidelater;

import productcatalog.home.HomeProductSuggestionsControllerComponent;
import productcatalog.home.SunriseHomePageController;

import javax.inject.Inject;

public class HomePageController extends SunriseHomePageController {
    @Inject
    public void registerProductSuggestions(final HomeProductSuggestionsControllerComponent component) {
        registerControllerComponent(component);
    }
}
