package controllers.shoppingcart;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.shoppingcart.checkout.thankyou.SunriseCheckoutThankYouController;

import javax.inject.Inject;

@LogMetrics
@NoCache
@RegisteredComponents(CheckoutStepControllerComponent.class)
public final class CheckoutThankYouController extends SunriseCheckoutThankYouController {

    @Inject
    public CheckoutThankYouController(final ContentRenderer contentRenderer) {
        super(contentRenderer);
    }

    @Override
    public String getTemplateName() {
        return "checkout-thankyou";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }
}
