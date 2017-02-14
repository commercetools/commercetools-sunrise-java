package demo.shoppingcart;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.checkout.payment.CheckoutPaymentExecutor;
import com.commercetools.sunrise.shoppingcart.checkout.payment.DefaultCheckoutPaymentFormData;
import com.commercetools.sunrise.shoppingcart.checkout.payment.PaymentSettings;
import com.commercetools.sunrise.shoppingcart.checkout.payment.SunriseCheckoutPaymentController;
import com.commercetools.sunrise.shoppingcart.checkout.payment.view.CheckoutPaymentPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class CheckoutPaymentController extends SunriseCheckoutPaymentController<DefaultCheckoutPaymentFormData> {

    private final CartReverseRouter cartReverseRouter;
    private final CheckoutReverseRouter checkoutReverseRouter;

    @Inject
    public CheckoutPaymentController(final RequestHookContext hookContext,
                                     final TemplateRenderer templateRenderer,
                                     final FormFactory formFactory,
                                     final CartFinder cartFinder,
                                     final CheckoutPaymentExecutor checkoutPaymentExecutor,
                                     final CheckoutPaymentPageContentFactory checkoutPaymentPageContentFactory,
                                     final PaymentSettings paymentSettings,
                                     final CartReverseRouter cartReverseRouter,
                                     final CheckoutReverseRouter checkoutReverseRouter) {
        super(hookContext, templateRenderer, formFactory, cartFinder, checkoutPaymentExecutor, checkoutPaymentPageContentFactory, paymentSettings);
        this.cartReverseRouter = cartReverseRouter;
        this.checkoutReverseRouter = checkoutReverseRouter;
    }

    @Override
    public Class<DefaultCheckoutPaymentFormData> getFormDataClass() {
        return DefaultCheckoutPaymentFormData.class;
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.showCart());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final DefaultCheckoutPaymentFormData formData) {
        return redirectTo(checkoutReverseRouter.checkoutConfirmationPageCall());
    }
}