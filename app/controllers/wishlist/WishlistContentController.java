package controllers.wishlist;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.wishlist.content.SunriseWishlistContentController;
import com.commercetools.sunrise.wishlist.content.viewmodels.WishlistPageContentFactory;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class WishlistContentController extends SunriseWishlistContentController {

    @Inject
    public WishlistContentController(final ContentRenderer contentRenderer,
                                     final WishlistPageContentFactory wishlistPageContentFactory) {
        super(contentRenderer, wishlistPageContentFactory);
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
