package com.commercetools.sunrise.wishlist.clear;

import com.commercetools.sunrise.core.controllers.SunriseController;
import com.commercetools.sunrise.core.controllers.WithExecutionFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.wishlist.WishlistReverseRouter;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

/**
 * This controller is used to view the current wishlist.
 */
public abstract class SunriseClearWishlistController extends SunriseController implements WithExecutionFlow<Void, ShoppingList> {

    private final ClearWishlistControllerAction controllerAction;

    @Inject
    protected SunriseClearWishlistController(final ClearWishlistControllerAction controllerAction) {
        this.controllerAction = controllerAction;
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.CLEAR_WISHLIST_PROCESS)
    public CompletionStage<Result> process() {
        return processRequest(null);
    }

    @Override
    public CompletionStage<ShoppingList> executeAction(final Void input) {
        return controllerAction.apply();
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist);

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Void input, final ClientErrorException clientErrorException) {
        return handleGeneralFailedAction(clientErrorException);
    }
}
