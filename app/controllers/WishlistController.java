package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.wishlist.AddToWishlistFormAction;
import com.commercetools.sunrise.wishlist.RemoveFromWishlistFormAction;
import com.commercetools.sunrise.wishlist.SunriseWishlistController;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class WishlistController extends SunriseWishlistController {

    @Inject
    WishlistController(final TemplateEngine templateEngine,
                       final AddToWishlistFormAction addToWishlistFormAction,
                       final RemoveFromWishlistFormAction removeFromWishlistFormAction) {
        super(templateEngine, addToWishlistFormAction, removeFromWishlistFormAction);
    }

    @Override
    protected Result onLineItemAdded() {
        return redirect(routes.WishlistController.show());
    }

    @Override
    protected Result onLineItemRemoved() {
        return redirect(routes.WishlistController.show());
    }
}
