package controllers.shoppingcart;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.models.carts.CartPaymentInfoExpansionComponent;
import com.commercetools.sunrise.models.carts.CartShippingInfoExpansionComponent;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.CheckoutConfirmationFormAction;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.CheckoutConfirmationFormData;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.SunriseCheckoutConfirmationController;
import io.sphere.sdk.orders.Order;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        CheckoutStepControllerComponent.class,
        CartShippingInfoExpansionComponent.class,
        CartPaymentInfoExpansionComponent.class
})
public final class CheckoutConfirmationController extends SunriseCheckoutConfirmationController {

    @Inject
    public CheckoutConfirmationController(final ContentRenderer contentRenderer,
                                          final FormFactory formFactory,
                                          final CheckoutConfirmationFormData formData,
                                          final CheckoutConfirmationFormAction controllerAction) {
        super(contentRenderer, formFactory, formData, controllerAction);
    }

    @Override
    public String getTemplateName() {
        return "checkout-confirmation";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Order order, final CheckoutConfirmationFormData formData) {
        return redirectAsync(routes.CheckoutThankYouController.show());
    }
}
