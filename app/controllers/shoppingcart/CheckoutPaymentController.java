package controllers.shoppingcart;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import com.commercetools.sunrise.models.carts.CartFetcher;
import com.commercetools.sunrise.models.carts.CartPaymentInfoExpansionComponent;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.shoppingcart.checkout.payment.CheckoutPaymentControllerAction;
import com.commercetools.sunrise.shoppingcart.checkout.payment.CheckoutPaymentFormData;
import com.commercetools.sunrise.models.payments.PaymentSettings;
import com.commercetools.sunrise.shoppingcart.checkout.payment.SunriseCheckoutPaymentController;
import com.commercetools.sunrise.shoppingcart.checkout.payment.viewmodels.CheckoutPaymentPageContentFactory;
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

    private final CartReverseRouter cartReverseRouter;
    private final CheckoutReverseRouter checkoutReverseRouter;

    @Inject
    public CheckoutPaymentController(final ContentRenderer contentRenderer,
                                     final FormFactory formFactory,
                                     final CheckoutPaymentFormData formData,
                                     final CartFetcher cartFetcher,
                                     final CheckoutPaymentControllerAction controllerAction,
                                     final CheckoutPaymentPageContentFactory pageContentFactory,
                                     final PaymentSettings paymentSettings,
                                     final CartReverseRouter cartReverseRouter,
                                     final CheckoutReverseRouter checkoutReverseRouter) {
        super(contentRenderer, formFactory, formData, cartFetcher, controllerAction, pageContentFactory, paymentSettings);
        this.cartReverseRouter = cartReverseRouter;
        this.checkoutReverseRouter = checkoutReverseRouter;
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
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectToCall(cartReverseRouter.cartDetailPageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final CheckoutPaymentFormData formData) {
        return redirectToCall(checkoutReverseRouter.checkoutConfirmationPageCall());
    }
}