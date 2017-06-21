package controllers.productcatalog;


import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.reverserouters.productcatalog.home.HomeReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.productcatalog.home.HomeRecommendationsControllerComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;
import com.commercetools.sunrise.productcatalog.home.viewmodels.HomePageContentFactory;
import com.commercetools.sunrise.wishlist.MiniWishlistControllerComponent;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        HomeRecommendationsControllerComponent.class,
        MiniWishlistControllerComponent.class
})
public final class HomeController extends SunriseHomeController {

    private final HomeReverseRouter homeReverseRouter;

    @Inject
    public HomeController(final ContentRenderer contentRenderer,
                          final HomePageContentFactory pageContentFactory,
                          final HomeReverseRouter homeReverseRouter) {
        super(contentRenderer, pageContentFactory);
        this.homeReverseRouter = homeReverseRouter;
    }

    @Nullable
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
        return redirectToCall(homeReverseRouter.homePageCall());
    }
}
