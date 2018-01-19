package controllers;

import com.commercetools.sunrise.cms.DefaultCmsComponent;
import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.components.DisableComponents;
import com.commercetools.sunrise.core.components.EnableComponents;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.models.products.NewProductFlagComponent;
import com.commercetools.sunrise.models.products.ProductAvailabilityComponent;
import com.commercetools.sunrise.productcatalog.home.HomeCmsComponent;
import com.commercetools.sunrise.productcatalog.home.HomeSuggestionsComponent;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@DisableComponents(DefaultCmsComponent.class)
@EnableComponents({
        HomeCmsComponent.class,
        HomeSuggestionsComponent.class,
        NewProductFlagComponent.class,
        ProductAvailabilityComponent.class
})
public class HomeController extends SunriseController {

    private static final String TEMPLATE = "home";

    private final TemplateEngine templateEngine;

    @Inject
    HomeController(final TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @EnableHooks
    public CompletionStage<Result> show() {
        return templateEngine.render(TEMPLATE).thenApply(Results::ok);
    }
}
