package demo.productcatalog;


import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.productcatalog.home.HomeProductSuggestionsControllerComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;
import com.commercetools.sunrise.productcatalog.home.view.HomePageContentFactory;
import demo.CommonControllerComponentListSupplier;
import demo.PageHeaderControllerComponentListSupplier;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class HomeController extends SunriseHomeController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public HomeController(final ComponentRegistry componentRegistry,
                          final TemplateRenderer templateRenderer,
                          final HomePageContentFactory homePageContentFactory,
                          final HomeReverseRouter homeReverseRouter) {
        super(componentRegistry, templateRenderer, homePageContentFactory);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Inject
    public void registerComponents(final CommonControllerComponentListSupplier commonControllerComponentListSupplier,
                                   final PageHeaderControllerComponentListSupplier pageHeaderControllerComponentListSupplier,
                                   final HomeProductSuggestionsControllerComponent homeProductSuggestionsControllerComponent) {
        register(commonControllerComponentListSupplier);
        register(pageHeaderControllerComponentListSupplier);
        register(homeProductSuggestionsControllerComponent);
    }

    @Override
    public String getCmsPageKey() {
        return "home";
    }

    public CompletionStage<Result> index() {
        return redirectTo(homeReverseRouter.homePageCall());
    }
}
