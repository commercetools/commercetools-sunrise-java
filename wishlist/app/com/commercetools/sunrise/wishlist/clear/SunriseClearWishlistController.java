package com.commercetools.sunrise.wishlist.clear;

import com.commercetools.sunrise.core.controllers.SunriseController;
import com.commercetools.sunrise.core.controllers.WithExecutionFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.wishlist.WishlistFinder;
import com.commercetools.sunrise.wishlist.WithRequiredWishlist;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller is used to view the current wishlist.
 */
public abstract class SunriseClearWishlistController extends SunriseController
        implements WithExecutionFlow<ShoppingList, ShoppingList>, WithRequiredWishlist {

    private final WishlistFinder wishlistFinder;
    private final ClearWishlistControllerAction controllerAction;

    @Inject
    protected SunriseClearWishlistController(final WishlistFinder wishlistFinder,
                                             final ClearWishlistControllerAction controllerAction) {
        this.wishlistFinder = wishlistFinder;
        this.controllerAction = controllerAction;
    }

    @Override
    public final WishlistFinder getWishlistFinder() {
        return wishlistFinder;
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.CLEAR_WISHLIST_PROCESS)
    public CompletionStage<Result> process() {
        return requireWishlist(this::processRequest);
    }

    @Override
    public CompletionStage<ShoppingList> executeAction(final ShoppingList wishlist) {
        return controllerAction.apply(wishlist);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist);

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final ShoppingList wishlist, final ClientErrorException clientErrorException) {
        return handleGeneralFailedAction(clientErrorException);
    }
}
