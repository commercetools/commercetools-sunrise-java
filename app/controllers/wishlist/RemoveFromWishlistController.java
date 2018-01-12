package controllers.wishlist;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.wishlist.remove.RemoveFromWishlistFormAction;
import com.commercetools.sunrise.wishlist.remove.RemoveFromWishlistFormData;
import com.commercetools.sunrise.wishlist.remove.SunriseRemoveFromWishlistController;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
public final class RemoveFromWishlistController extends SunriseRemoveFromWishlistController {

    @Inject
    public RemoveFromWishlistController(final ContentRenderer contentRenderer,
                                        final FormFactory formFactory,
                                        final RemoveFromWishlistFormData formData,
                                        final RemoveFromWishlistFormAction controllerAction) {
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
    public CompletionStage<Result> handleSuccessfulAction(final ShoppingList wishlist, final RemoveFromWishlistFormData removeFromWishlistFormData) {
        return redirectAsync(routes.WishlistContentController.show());
    }
}
