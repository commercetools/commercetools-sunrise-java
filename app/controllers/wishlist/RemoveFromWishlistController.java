package controllers.wishlist;

import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.wishlist.WishlistFinder;
import com.commercetools.sunrise.wishlist.controllers.RemoveFromWishlistControllerAction;
import com.commercetools.sunrise.wishlist.controllers.SunriseRemoveFromWishlistController;
import com.commercetools.sunrise.wishlist.controllers.WishlistInSessionControllerComponent;
import com.commercetools.sunrise.wishlist.viewmodels.RemoveWishlistLineItemFormData;
import com.commercetools.sunrise.wishlist.viewmodels.WishlistPageContentFactory;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@RegisteredComponents(WishlistInSessionControllerComponent.class)
public final class RemoveFromWishlistController extends SunriseRemoveFromWishlistController {
    private final WishlistReverseRouter reverseRouter;

    @Inject
    public RemoveFromWishlistController(final ContentRenderer contentRenderer,
                                        final FormFactory formFactory,
                                        final WishlistPageContentFactory wishlistPageContentFactory,
                                        final RemoveWishlistLineItemFormData formData,
                                        final WishlistFinder wishlistFinder,
                                        final RemoveFromWishlistControllerAction controllerAction,
                                        final WishlistReverseRouter reverseRouter) {
        super(contentRenderer, formFactory, wishlistPageContentFactory, formData, wishlistFinder, controllerAction);
        this.reverseRouter = reverseRouter;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist, final RemoveWishlistLineItemFormData removeWishlistLineItemFormData) {
        return redirectToCall(reverseRouter.wishlistPageCall());
    }

    @Override
    public CompletionStage<Result> handleNotFoundWishlist() {
        return redirectToCall(reverseRouter.wishlistPageCall());
    }
}
