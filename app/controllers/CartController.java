package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.shoppingcart.carts.AddToCartFormAction;
import com.commercetools.sunrise.shoppingcart.carts.ChangeQuantityInCartFormAction;
import com.commercetools.sunrise.shoppingcart.carts.RemoveFromCartFormAction;
import com.commercetools.sunrise.shoppingcart.carts.SunriseCartsController;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
public final class CartController extends SunriseCartsController {

    @Inject
    CartController(final TemplateEngine templateEngine,
                   final AddToCartFormAction addToCartFormAction,
                   final ChangeQuantityInCartFormAction changeQuantityInCartFormAction,
                   final RemoveFromCartFormAction removeFromCartFormAction) {
        super(templateEngine, addToCartFormAction, changeQuantityInCartFormAction, removeFromCartFormAction);
    }

    @Override
    protected Result onLineItemAdded() {
        return redirect(routes.CartController.show());
    }

    @Override
    protected Result onQuantityChanged() {
        return redirect(routes.CartController.show());
    }

    @Override
    protected Result onLineItemRemoved() {
        return redirect(routes.CartController.show());
    }
}
