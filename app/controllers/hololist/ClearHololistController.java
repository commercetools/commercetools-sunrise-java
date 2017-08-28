package controllers.hololist;

import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.shoppinglist.ShoppingListFinder;
import com.commercetools.sunrise.shoppinglist.ShoppingListInSessionControllerComponent;
import com.commercetools.sunrise.shoppinglist.clear.ClearShoppingListControllerAction;
import com.commercetools.sunrise.shoppinglist.clear.SunriseClearShoppingListController;
import com.google.inject.Inject;
import com.commercetools.sunrise.framework.reverserouters.hololist.HololistReverseRouter;
import controllers.wishlist.WishlistTypeIdentifier;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents(ShoppingListInSessionControllerComponent.class)
public final class ClearHololistController extends SunriseClearShoppingListController implements HololistTypeIdentifier {

    private final HololistReverseRouter reverseRouter;

    @Inject
    public ClearHololistController(final ShoppingListFinder wishlistFinder,
                                   final ClearShoppingListControllerAction controllerAction,
                                   final HololistReverseRouter reverseRouter) {
        super(wishlistFinder, controllerAction);
        this.reverseRouter = reverseRouter;
    }

    @EnableHooks
    @SunriseRoute(HololistReverseRouter.CLEAR_HOLOLIST_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireShoppingList(this::processRequest, getShoppingListType());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist) {
        return redirectToCall(reverseRouter.hololistPageCall());
    }

    @Override
    public CompletionStage<Result> handleNotFoundShoppingList() {
        return redirectToCall(reverseRouter.hololistPageCall());
    }
}
