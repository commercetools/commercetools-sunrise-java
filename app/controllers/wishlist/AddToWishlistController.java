package controllers.wishlist;

import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.wishlist.WishlistCreator;
import com.commercetools.sunrise.wishlist.WishlistFinder;
import com.commercetools.sunrise.wishlist.controllers.AddToWishlistControllerAction;
import com.commercetools.sunrise.wishlist.controllers.SunriseAddToWishlistController;
import com.commercetools.sunrise.wishlist.controllers.WishlistInSessionControllerComponent;
import com.commercetools.sunrise.wishlist.viewmodels.AddWishlistLineItemFormData;
import com.commercetools.sunrise.wishlist.viewmodels.WishlistPageContentFactory;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@RegisteredComponents(WishlistInSessionControllerComponent.class)
public final class AddToWishlistController extends SunriseAddToWishlistController {
    private final WishlistReverseRouter reverseRouter;

    @Inject
    public AddToWishlistController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                   final WishlistPageContentFactory wishlistPageContentFactory,
                                   final AddWishlistLineItemFormData formData, final WishlistCreator wishlistCreator, final WishlistFinder wishlistFinder,
                                   final AddToWishlistControllerAction controllerAction, final WishlistReverseRouter reverseRouter) {
        super(contentRenderer, formFactory, wishlistPageContentFactory, formData, wishlistCreator, wishlistFinder, controllerAction);
        this.reverseRouter = reverseRouter;
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist, final AddWishlistLineItemFormData addWishlistLineItemFormData) {
        return redirectToCall(reverseRouter.wishlistPageCall());
    }
}
