package controllers.wishlist;

import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.wishlist.WishlistFinder;
import com.commercetools.sunrise.wishlist.WishlistInSessionControllerComponent;
import com.commercetools.sunrise.wishlist.content.SunriseWishlistContentController;
import com.commercetools.sunrise.wishlist.content.viewmodels.WishlistPageContentFactory;

import javax.annotation.Nullable;
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

    @Nullable
    @Override
    public String getTemplateName() {
        return "my-account-wishlist";
    }
}
