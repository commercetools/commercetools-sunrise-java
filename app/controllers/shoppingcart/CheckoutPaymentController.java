package controllers.shoppingcart;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.models.carts.CartPaymentInfoExpansionComponent;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.shoppingcart.checkout.payment.CheckoutPaymentControllerAction;
import com.commercetools.sunrise.shoppingcart.checkout.payment.CheckoutPaymentFormData;
import com.commercetools.sunrise.shoppingcart.checkout.payment.SunriseCheckoutPaymentController;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents({
        CheckoutStepControllerComponent.class,
        CartPaymentInfoExpansionComponent.class
})
public final class CheckoutPaymentController extends SunriseCheckoutPaymentController {

    @Inject
    public CheckoutPaymentController(final ContentRenderer contentRenderer,
                                     final FormFactory formFactory,
                                     final CheckoutPaymentFormData formData,
                                     final CheckoutPaymentControllerAction controllerAction) {
        super(contentRenderer, formFactory, formData, controllerAction);
    }

    @Override
    public String getTemplateName() {
        return "checkout-payment";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final CheckoutPaymentFormData formData) {
        return redirectAsync(routes.CheckoutConfirmationController.show());
    }
}