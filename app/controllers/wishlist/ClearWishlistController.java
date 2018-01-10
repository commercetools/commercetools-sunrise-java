package controllers.wishlist;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.wishlist.clear.ClearWishlistControllerAction;
import com.commercetools.sunrise.wishlist.clear.SunriseClearWishlistController;
import com.google.inject.Inject;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class ClearWishlistController extends SunriseClearWishlistController {

    @Inject
    public ClearWishlistController(final ClearWishlistControllerAction controllerAction) {
        super(controllerAction);
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist) {
        return redirectAsync(routes.WishlistContentController.show());
    }
}
