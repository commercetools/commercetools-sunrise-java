package com.commercetools.sunrise.wishlist.controllers;

import com.commercetools.sunrise.framework.controllers.SunriseContentController;
import com.commercetools.sunrise.framework.controllers.WithQueryFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.wishlist.WishlistFinder;
import com.commercetools.sunrise.wishlist.WithRequiredWishlist;
import com.commercetools.sunrise.wishlist.viewmodels.WishlistPageContentFactory;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller is used to view the current wishlist.
 */
public abstract class SunriseWishlistController extends SunriseContentController implements WithQueryFlow<ShoppingList>, WithRequiredWishlist {
    private final WishlistFinder wishlistFinder;
    private final WishlistPageContentFactory wishlistPageContentFactory;

    @Inject
    protected SunriseWishlistController(final ContentRenderer contentRenderer,
                                        final WishlistPageContentFactory wishlistPageContentFactory,
                                        final WishlistFinder wishlistFinder) {
        super(contentRenderer);
        this.wishlistPageContentFactory = wishlistPageContentFactory;
        this.wishlistFinder = wishlistFinder;
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.WISHLIST_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireWishlist(this::showPage);
    }

    @Override
    public PageContent createPageContent(final ShoppingList wishlist) {
        return wishlistPageContentFactory.create(wishlist);
    }

    @Override
    public WishlistFinder getWishlistFinder() {
        return wishlistFinder;
    }

    @Override
    public CompletionStage<Result> handleNotFoundWishlist() {
        return okResultWithPageContent(wishlistPageContentFactory.create(null));
    }
}
