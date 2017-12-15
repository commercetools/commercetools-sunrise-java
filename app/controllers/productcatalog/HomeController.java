package controllers.productcatalog;


import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.renderers.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.renderers.ContentRenderer;
import com.commercetools.sunrise.productcatalog.home.HomeRecommendationsControllerComponent;
import com.commercetools.sunrise.productcatalog.home.SunriseHomeController;
import com.commercetools.sunrise.productcatalog.home.viewmodels.HomePageContentFactory;
import com.commercetools.sunrise.wishlist.MiniWishlistControllerComponent;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        HomeRecommendationsControllerComponent.class,
        MiniWishlistControllerComponent.class
})
public final class HomeController extends SunriseHomeController {

    @Inject
    public HomeController(final ContentRenderer contentRenderer,
                          final HomePageContentFactory pageContentFactory) {
        super(contentRenderer, pageContentFactory);
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
