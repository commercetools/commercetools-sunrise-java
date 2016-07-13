package demo.productcatalog;


import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.productcatalog.home.HomeProductSuggestionsControllerComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;

import javax.inject.Inject;

@RequestScoped
public class HomeController extends SunriseHomeController {
    @Inject
    public void registerProductSuggestions(final HomeProductSuggestionsControllerComponent component) {
        registerControllerComponent(component);
    }
}
