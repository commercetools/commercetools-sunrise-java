package com.commercetools.sunrise.wishlist.controllers;

import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithExecutionFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.wishlist.WishlistFinder;
import com.commercetools.sunrise.wishlist.WithRequiredWishlist;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller is used to view the current wishlist.
 */
public abstract class SunriseClearWishlistController extends SunriseContentFormController
        implements WithExecutionFlow<ShoppingList, ShoppingList>, WithRequiredWishlist {
    private final WishlistFinder wishlistFinder;
    private final ClearWishlistControllerAction controllerAction;

    @Inject
    protected SunriseClearWishlistController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                             final WishlistFinder wishlistFinder,
                                             final ClearWishlistControllerAction controllerAction) {
        super(contentRenderer, formFactory);
        this.wishlistFinder = wishlistFinder;
        this.controllerAction = controllerAction;
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.CLEAR_WISHLIST_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireWishlist(this::processRequest);
    }

    @Override
    public CompletionStage<ShoppingList> executeAction(final ShoppingList wishlist) {
        return controllerAction.apply(wishlist);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist);

    @Override
    public WishlistFinder getWishlistFinder() {
        return wishlistFinder;
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final ShoppingList wishlist, final ClientErrorException clientErrorException) {
        return handleGeneralFailedAction(clientErrorException);
    }
}
