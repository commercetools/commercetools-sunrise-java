package controllers.shoppingcart;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.models.carts.CartShippingInfoExpansionComponent;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.CheckoutShippingControllerAction;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.CheckoutShippingFormData;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.SunriseCheckoutShippingController;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        CheckoutStepControllerComponent.class,
        CartShippingInfoExpansionComponent.class
})
public final class CheckoutShippingController extends SunriseCheckoutShippingController {

    @Inject
    public CheckoutShippingController(final ContentRenderer contentRenderer,
                                      final FormFactory formFactory,
                                      final CheckoutShippingFormData formData,
                                      final CheckoutShippingControllerAction controllerAction) {
        super(contentRenderer, formFactory, formData, controllerAction);
    }

    @Override
    public String getTemplateName() {
        return "checkout-shipping";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final CheckoutShippingFormData formData) {
        return redirectAsync(routes.CheckoutPaymentController.show());
    }
}
