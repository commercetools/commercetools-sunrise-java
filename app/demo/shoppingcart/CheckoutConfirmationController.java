package demo.shoppingcart;

import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.CheckoutConfirmationExecutor;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.DefaultCheckoutConfirmationFormData;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.SunriseCheckoutConfirmationController;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.view.CheckoutConfirmationPageContentFactory;
import io.sphere.sdk.orders.Order;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class CheckoutConfirmationController extends SunriseCheckoutConfirmationController<DefaultCheckoutConfirmationFormData> {

    private final CartReverseRouter cartReverseRouter;
    private final CheckoutReverseRouter checkoutReverseRouter;

    @Inject
    public CheckoutConfirmationController(final TemplateRenderer templateRenderer,
                                          final RequestHookContext hookContext,
                                          final CartFinder cartFinder,
                                          final FormFactory formFactory,
                                          final CheckoutConfirmationExecutor checkoutConfirmationExecutor,
                                          final CheckoutConfirmationPageContentFactory checkoutConfirmationPageContentFactory,
                                          final CartReverseRouter cartReverseRouter,
                                          final CheckoutReverseRouter checkoutReverseRouter) {
        super(templateRenderer, hookContext, cartFinder, formFactory, checkoutConfirmationExecutor, checkoutConfirmationPageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
        this.checkoutReverseRouter = checkoutReverseRouter;
    }

    @Override
    public Class<DefaultCheckoutConfirmationFormData> getFormDataClass() {
        return DefaultCheckoutConfirmationFormData.class;
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.showCart());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Order order, final DefaultCheckoutConfirmationFormData formData) {
        return redirectTo(checkoutReverseRouter.checkoutThankYouPageCall());
    }
}
