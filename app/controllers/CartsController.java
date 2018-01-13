package controllers;

import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.shoppingcart.carts.AddToCartFormAction;
import com.commercetools.sunrise.shoppingcart.carts.ChangeQuantityInCartFormAction;
import com.commercetools.sunrise.shoppingcart.carts.RemoveFromCartFormAction;
import com.commercetools.sunrise.shoppingcart.carts.SunriseCartsController;
import controllers.shoppingcart.routes;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class CartsController extends SunriseCartsController {

    @Inject
    CartsController(final TemplateEngine templateEngine,
                    final AddToCartFormAction addToCartFormAction,
                    final ChangeQuantityInCartFormAction changeQuantityInCartFormAction,
                    final RemoveFromCartFormAction removeFromCartFormAction) {
        super(templateEngine, addToCartFormAction, changeQuantityInCartFormAction, removeFromCartFormAction);
    }

    @Override
    protected Result onLineItemAdded() {
        return redirect(controllers.shoppingcart.routes.CartsController.show());
    }

    @Override
    protected Result onQuantityChanged() {
        return redirect(controllers.shoppingcart.routes.CartsController.show());
    }

    @Override
    protected Result onLineItemRemoved() {
        return redirect(controllers.shoppingcart.routes.CartsController.show());
    }
}
