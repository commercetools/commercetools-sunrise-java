package demo.shoppingcart;

import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.CheckoutConfirmationExecutor;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.DefaultCheckoutConfirmationFormData;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.SunriseCheckoutConfirmationController;
import com.commercetools.sunrise.shoppingcart.checkout.confirmation.view.CheckoutConfirmationPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.orders.Order;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class CheckoutConfirmationController extends SunriseCheckoutConfirmationController<DefaultCheckoutConfirmationFormData> {

    private final CartReverseRouter cartReverseRouter;
    private final CheckoutReverseRouter checkoutReverseRouter;

    @Inject
    public CheckoutConfirmationController(final CartFinder cartFinder,
                                          final CheckoutConfirmationExecutor checkoutConfirmationExecutor,
                                          final CheckoutConfirmationPageContentFactory checkoutConfirmationPageContentFactory,
                                          final CartReverseRouter cartReverseRouter,
                                          final CheckoutReverseRouter checkoutReverseRouter) {
        super(cartFinder, checkoutConfirmationExecutor, checkoutConfirmationPageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
        this.checkoutReverseRouter = checkoutReverseRouter;
    }

    @Override
    public Class<DefaultCheckoutConfirmationFormData> getFormDataClass() {
        return DefaultCheckoutConfirmationFormData.class;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.showCart());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final DefaultCheckoutConfirmationFormData formData, final Cart cart, final Order order) {
        return redirectTo(checkoutReverseRouter.checkoutThankYouPageCall());
    }
}
