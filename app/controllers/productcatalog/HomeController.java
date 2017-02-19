package controllers.productcatalog;


import com.commercetools.sunrise.common.ctp.metrics.LogMetrics;
import com.commercetools.sunrise.framework.components.CommonControllerComponentsSupplier;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.HomeReverseRouter;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.productcatalog.home.HomeRecommendationsControllerComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;
import com.commercetools.sunrise.productcatalog.home.view.HomePageContentFactory;
import controllers.PageHeaderControllerComponentsSupplier;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        HomeRecommendationsControllerComponent.class,
        CommonControllerComponentsSupplier.class,
        PageHeaderControllerComponentsSupplier.class
})
public final class HomeController extends SunriseHomeController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public HomeController(final TemplateRenderer templateRenderer,
                          final HomePageContentFactory homePageContentFactory,
                          final HomeReverseRouter homeReverseRouter) {
        super(templateRenderer, homePageContentFactory);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "home";
    }

    @Nullable
    @Override
    public String getCmsPageKey() {
        return "home";
    }

    public CompletionStage<Result> index() {
        return redirectTo(homeReverseRouter.homePageCall());
    }
}
