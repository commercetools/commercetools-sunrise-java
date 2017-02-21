package controllers.shoppingcart;

import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CartReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CheckoutReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.framework.checkout.payment.CheckoutPaymentControllerAction;
import com.commercetools.sunrise.framework.checkout.payment.DefaultCheckoutPaymentFormData;
import com.commercetools.sunrise.framework.checkout.payment.PaymentSettings;
import com.commercetools.sunrise.framework.checkout.payment.SunriseCheckoutPaymentController;
import com.commercetools.sunrise.framework.checkout.payment.viewmodels.CheckoutPaymentPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        CheckoutStepControllerComponent.class,
        CartOperationsControllerComponentSupplier.class
})
public final class CheckoutPaymentController extends SunriseCheckoutPaymentController<DefaultCheckoutPaymentFormData> {

    private final CartReverseRouter cartReverseRouter;
    private final CheckoutReverseRouter checkoutReverseRouter;

    @Inject
    public CheckoutPaymentController(final TemplateRenderer templateRenderer,
                                     final FormFactory formFactory,
                                     final CartFinder cartFinder,
                                     final CheckoutPaymentControllerAction checkoutPaymentControllerAction,
                                     final CheckoutPaymentPageContentFactory checkoutPaymentPageContentFactory,
                                     final PaymentSettings paymentSettings,
                                     final CartReverseRouter cartReverseRouter,
                                     final CheckoutReverseRouter checkoutReverseRouter) {
        super(templateRenderer, formFactory, cartFinder, checkoutPaymentControllerAction, checkoutPaymentPageContentFactory, paymentSettings);
        this.cartReverseRouter = cartReverseRouter;
        this.checkoutReverseRouter = checkoutReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "checkout-payment";
    }

    @Override
    public Class<DefaultCheckoutPaymentFormData> getFormDataClass() {
        return DefaultCheckoutPaymentFormData.class;
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.cartDetailPageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final DefaultCheckoutPaymentFormData formData) {
        return redirectTo(checkoutReverseRouter.checkoutConfirmationPageCall());
    }
}