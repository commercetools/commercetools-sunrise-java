package controllers;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.models.carts.CartPaymentInfoExpansionComponent;
import com.commercetools.sunrise.models.carts.CartShippingInfoExpansionComponent;
import com.commercetools.sunrise.shoppingcart.checkout.*;
import play.mvc.Result;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents({
        CartShippingInfoExpansionComponent.class,
        CartPaymentInfoExpansionComponent.class
})
public final class CheckoutController extends SunriseCheckoutController {

    @Inject
    CheckoutController(final TemplateEngine templateEngine,
                       final SetAddressFormAction setAddressFormAction,
                       final SetShippingFormAction setShippingFormAction,
                       final SetPaymentFormAction setPaymentFormAction,
                       final PlaceOrderFormAction placeOrderFormAction) {
        super(templateEngine, setAddressFormAction, setShippingFormAction, setPaymentFormAction, placeOrderFormAction);
    }

    @Override
    protected Result onAddressSet() {
        return redirect(routes.CheckoutController.showSetShippingForm());
    }

    @Override
    protected Result onShippingSet() {
        return redirect(routes.CheckoutController.showSetPaymentForm());
    }

    @Override
    protected Result onPaymentSet() {
        return redirect(routes.CheckoutController.showPlaceOrderForm());
    }

    @Override
    protected Result onOrderPlaced() {
        return redirect(routes.CheckoutController.showOrder());
    }
}