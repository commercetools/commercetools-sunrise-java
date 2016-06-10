package wedecidelater;

import com.commercetools.sunrise.productcatalog.home.HomeProductSuggestionsControllerComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomePageController;

import javax.inject.Inject;

public class HomePageController extends SunriseHomePageController {
    @Inject
    public void registerProductSuggestions(final HomeProductSuggestionsControllerComponent component) {
        registerControllerComponent(component);
    }
}
