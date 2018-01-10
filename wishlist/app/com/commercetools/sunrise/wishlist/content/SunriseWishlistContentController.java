package com.commercetools.sunrise.wishlist.content;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.wishlist.content.viewmodels.WishlistPageContentFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller is used to view the current wishlist.
 */
public abstract class SunriseWishlistContentController extends SunriseContentController implements WithQueryFlow<Void> {

    private final WishlistPageContentFactory wishlistPageContentFactory;

    @Inject
    protected SunriseWishlistContentController(final ContentRenderer contentRenderer,
                                               final WishlistPageContentFactory wishlistPageContentFactory) {
        super(contentRenderer);
        this.wishlistPageContentFactory = wishlistPageContentFactory;
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.WISHLIST_PAGE)
    public CompletionStage<Result> show() {
        return showPage(null);
    }

    @Override
    public PageContent createPageContent(final Void input) {
        return wishlistPageContentFactory.create(null);
    }
}
