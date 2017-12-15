package controllers.wishlist;

import com.commercetools.sunrise.core.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.core.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.wishlist.WishlistFinder;
import com.commercetools.sunrise.wishlist.WishlistInSessionControllerComponent;
import com.commercetools.sunrise.wishlist.content.SunriseWishlistContentController;
import com.commercetools.sunrise.wishlist.content.viewmodels.WishlistPageContentFactory;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        WishlistInSessionControllerComponent.class
})
public final class WishlistContentController extends SunriseWishlistContentController {

    @Inject
    public WishlistContentController(final ContentRenderer contentRenderer,
                                     final WishlistPageContentFactory wishlistPageContentFactory,
                                     final WishlistFinder wishlistFinder) {
        super(contentRenderer, wishlistPageContentFactory, wishlistFinder);
    }

    @Override
    public String getTemplateName() {
        return "my-account-wishlist";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }
}
