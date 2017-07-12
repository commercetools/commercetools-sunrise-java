package controllers.wishlist;

import com.commercetools.sunrise.framework.components.controllers.PageHeaderControllerComponentSupplier;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.framework.reverserouters.wishlist.WishlistReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.wishlist.WishlistCreator;
import com.commercetools.sunrise.wishlist.WishlistFinder;
import com.commercetools.sunrise.wishlist.WishlistInSessionControllerComponent;
import com.commercetools.sunrise.wishlist.add.AddToWishlistControllerAction;
import com.commercetools.sunrise.wishlist.add.AddToWishlistFormData;
import com.commercetools.sunrise.wishlist.add.SunriseAddToWishlistController;
import com.commercetools.sunrise.wishlist.content.viewmodels.WishlistPageContentFactory;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        PageHeaderControllerComponentSupplier.class,
        WishlistInSessionControllerComponent.class
})
public final class AddToWishlistController extends SunriseAddToWishlistController {

    private final WishlistReverseRouter reverseRouter;

    @Inject
    public AddToWishlistController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                   final WishlistPageContentFactory wishlistPageContentFactory,
                                   final AddToWishlistFormData formData, final WishlistCreator wishlistCreator, final WishlistFinder wishlistFinder,
                                   final AddToWishlistControllerAction controllerAction, final WishlistReverseRouter reverseRouter) {
        super(contentRenderer, formFactory, wishlistPageContentFactory, formData, wishlistCreator, wishlistFinder, controllerAction);
        this.reverseRouter = reverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "my-account-wishlist";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist, final AddToWishlistFormData addToWishlistFormData) {
        return redirectToCall(reverseRouter.wishlistPageCall());
    }
}
