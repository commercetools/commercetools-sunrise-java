package controllers.wishlist;

import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.wishlist.WishlistFinder;
import com.commercetools.sunrise.wishlist.WishlistInSessionControllerComponent;
import com.commercetools.sunrise.wishlist.clear.ClearWishlistControllerAction;
import com.commercetools.sunrise.wishlist.clear.SunriseClearWishlistController;
import com.google.inject.Inject;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents(WishlistInSessionControllerComponent.class)
public final class ClearWishlistController extends SunriseClearWishlistController {

    private final WishlistReverseRouter reverseRouter;

    @Inject
    public ClearWishlistController(final WishlistFinder wishlistFinder,
                                   final ClearWishlistControllerAction controllerAction,
                                   final WishlistReverseRouter reverseRouter) {
        super(wishlistFinder, controllerAction);
        this.reverseRouter = reverseRouter;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist) {
        return redirectToCall(reverseRouter.wishlistPageCall());
    }

    @Override
    public CompletionStage<Result> handleNotFoundWishlist() {
        return redirectToCall(reverseRouter.wishlistPageCall());
    }
}
