package controllers.productcatalog;


import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.productcatalog.home.HomeRecommendationsControllerComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents(HomeRecommendationsControllerComponent.class)
public final class HomeController extends SunriseHomeController {

    @Inject
    public HomeController(final ContentRenderer contentRenderer) {
        super(contentRenderer);
    }

    @Override
    public String getTemplateName() {
        return "home";
    }

    @Override
    public String getCmsPageKey() {
        return "home";
    }
}
