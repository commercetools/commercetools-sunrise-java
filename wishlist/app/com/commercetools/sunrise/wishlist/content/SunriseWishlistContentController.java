package com.commercetools.sunrise.wishlist.content;

import com.commercetools.sunrise.core.controllers.SunriseContentController;
import com.commercetools.sunrise.core.controllers.WithQueryFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.wishlist.WishlistFinder;
import com.commercetools.sunrise.wishlist.WithRequiredWishlist;
import com.commercetools.sunrise.wishlist.content.viewmodels.WishlistPageContentFactory;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller is used to view the current wishlist.
 */
public abstract class SunriseWishlistContentController extends SunriseContentController implements WithQueryFlow<ShoppingList>, WithRequiredWishlist {

    private final WishlistFinder wishlistFinder;
    private final WishlistPageContentFactory wishlistPageContentFactory;

    @Inject
    protected SunriseWishlistContentController(final ContentRenderer contentRenderer,
                                               final WishlistPageContentFactory wishlistPageContentFactory,
                                               final WishlistFinder wishlistFinder) {
        super(contentRenderer);
        this.wishlistPageContentFactory = wishlistPageContentFactory;
        this.wishlistFinder = wishlistFinder;
    }

    @Override
    public final WishlistFinder getWishlistFinder() {
        return wishlistFinder;
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.WISHLIST_PAGE)
    public CompletionStage<Result> show() {
        return requireWishlist(this::showPage);
    }

    @Override
    public PageContent createPageContent(final ShoppingList wishlist) {
        return wishlistPageContentFactory.create(wishlist);
    }

    @Override
    public CompletionStage<Result> handleNotFoundWishlist() {
        return okResultWithPageContent(wishlistPageContentFactory.create(null));
    }
}
