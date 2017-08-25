package controllers.wishlist;

import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.wishlist.ShoppingListFinder;
import com.commercetools.sunrise.wishlist.ShoppingListInSessionControllerComponent;
import com.commercetools.sunrise.wishlist.clear.ClearShoppingListControllerAction;
import com.commercetools.sunrise.wishlist.clear.SunriseClearShoppingListController;
import com.google.inject.Inject;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents(ShoppingListInSessionControllerComponent.class)
public final class ClearWishlistController extends SunriseClearShoppingListController implements WishlistTypeIdentifier {

    private final WishlistReverseRouter reverseRouter;

    @Inject
    public ClearWishlistController(final ShoppingListFinder wishlistFinder,
                                   final ClearShoppingListControllerAction controllerAction,
                                   final WishlistReverseRouter reverseRouter) {
        super(wishlistFinder, controllerAction);
        this.reverseRouter = reverseRouter;
    }

    @EnableHooks
    @SunriseRoute(WishlistReverseRouter.CLEAR_WISHLIST_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireShoppingList(this::processRequest, getShoppingListType());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist) {
        return redirectToCall(reverseRouter.wishlistPageCall());
    }

    @Override
    public CompletionStage<Result> handleNotFoundShoppingList() {
        return redirectToCall(reverseRouter.wishlistPageCall());
    }
}
