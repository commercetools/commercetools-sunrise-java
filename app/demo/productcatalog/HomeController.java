package demo.productcatalog;


import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.productcatalog.home.HomeProductSuggestionsControllerComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;
import com.commercetools.sunrise.productcatalog.home.view.HomePageContentFactory;

import javax.inject.Inject;

public final class HomeController extends SunriseHomeController {

    @Inject
    public HomeController(final TemplateRenderer templateRenderer,
                          final RequestHookContext hookContext,
                          final HomePageContentFactory homePageContentFactory) {
        super(templateRenderer, hookContext, homePageContentFactory);
    }

    @Inject
    public void registerProductSuggestions(final HomeProductSuggestionsControllerComponent component) {
        registerControllerComponent(component);
    }
}
