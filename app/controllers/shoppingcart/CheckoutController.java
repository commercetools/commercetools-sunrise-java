package controllers.shoppingcart;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutAddressFormAction;
import com.commercetools.sunrise.shoppingcart.checkout.SunriseCheckoutController;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents(CheckoutStepControllerComponent.class)
public final class CheckoutController extends SunriseCheckoutController {

    @Inject
    public CheckoutController(final TemplateEngine templateEngine,
                              final CheckoutAddressFormAction controllerAction) {
        super(templateEngine, controllerAction);
    }
}