package controllers;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.productcatalog.home.HomeRecommendationsControllerComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents(HomeRecommendationsControllerComponent.class)
public final class HomeController extends SunriseHomeController {

    @Inject
    HomeController(final TemplateEngine templateEngine) {
        super(templateEngine);
    }
}
