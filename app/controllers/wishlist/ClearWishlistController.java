package controllers.wishlist;

import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.wishlist.WishlistFinder;
import com.commercetools.sunrise.wishlist.controllers.ClearWishlistControllerAction;
import com.commercetools.sunrise.wishlist.controllers.SunriseClearWishlistController;
import com.commercetools.sunrise.wishlist.controllers.WishlistInSessionControllerComponent;
import com.google.inject.Inject;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

@RegisteredComponents(WishlistInSessionControllerComponent.class)
public final class ClearWishlistController extends SunriseClearWishlistController {

    private final WishlistReverseRouter reverseRouter;

    @Inject
    public ClearWishlistController(final ContentRenderer contentRenderer,
                                   final FormFactory formFactory,
                                   final WishlistFinder wishlistFinder,
                                   final ClearWishlistControllerAction controllerAction,
                                   final WishlistReverseRouter reverseRouter) {
        super(contentRenderer, formFactory, wishlistFinder, controllerAction);
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
