package demo.productcatalog;


import com.commercetools.sunrise.productcatalog.home.view.HomePageContentFactory;
import com.commercetools.sunrise.productcatalog.home.HomeProductSuggestionsControllerComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;

import javax.inject.Inject;

public final class HomeController extends SunriseHomeController {

    @Inject
    public HomeController(final HomePageContentFactory homePageContentFactory) {
        super(homePageContentFactory);
    }

    @Inject
    public void registerProductSuggestions(final HomeProductSuggestionsControllerComponent component) {
        registerControllerComponent(component);
    }
}
