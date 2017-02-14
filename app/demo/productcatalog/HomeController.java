package demo.productcatalog;


import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.productcatalog.home.HomeProductSuggestionsControllerComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;
import com.commercetools.sunrise.productcatalog.home.view.HomePageContentFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class HomeController extends SunriseHomeController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public HomeController(final RequestHookContext hookContext,
                          final TemplateRenderer templateRenderer,
                          final HomePageContentFactory homePageContentFactory,
                          final HomeReverseRouter homeReverseRouter) {
        super(hookContext, templateRenderer, homePageContentFactory);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Inject
    public void registerProductSuggestions(final HomeProductSuggestionsControllerComponent component) {
        registerControllerComponent(component);
    }

    public CompletionStage<Result> index() {
        return redirectTo(homeReverseRouter.homePageCall());
    }
}
