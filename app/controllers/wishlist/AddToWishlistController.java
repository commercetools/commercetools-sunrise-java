package controllers.wishlist;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.wishlist.add.AddToWishlistFormAction;
import com.commercetools.sunrise.wishlist.add.AddToWishlistFormData;
import com.commercetools.sunrise.wishlist.add.SunriseAddToWishlistController;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class AddToWishlistController extends SunriseAddToWishlistController {

    @Inject
    public AddToWishlistController(final ContentRenderer contentRenderer,
                                   final FormFactory formFactory,
                                   final AddToWishlistFormData formData,
                                   final AddToWishlistFormAction controllerAction) {
        super(contentRenderer, formFactory, formData, controllerAction);
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
        return redirectAsync(routes.WishlistContentController.show());
    }
}
