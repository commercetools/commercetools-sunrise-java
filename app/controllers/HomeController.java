package controllers;

import com.commercetools.sunrise.cms.CmsComponent;
import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.components.DisableComponents;
import com.commercetools.sunrise.core.components.EnableComponents;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.productcatalog.home.HomeCmsComponent;
import com.commercetools.sunrise.productcatalog.home.HomeSuggestionsComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;

import javax.inject.Inject;

@LogMetrics
@NoCache
@DisableComponents(CmsComponent.class)
@EnableComponents({
        HomeCmsComponent.class,
        HomeSuggestionsComponent.class
})
public final class HomeController extends SunriseHomeController {

    @Inject
    HomeController(final TemplateEngine templateEngine) {
        super(templateEngine);
    }
}
