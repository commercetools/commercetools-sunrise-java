package demo.shoppingcart;

import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.checkout.address.CheckoutAddressExecutor;
import com.commercetools.sunrise.shoppingcart.checkout.address.DefaultCheckoutAddressFormData;
import com.commercetools.sunrise.shoppingcart.checkout.address.SunriseCheckoutAddressController;
import com.commercetools.sunrise.shoppingcart.checkout.address.view.CheckoutAddressPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class CheckoutAddressController extends SunriseCheckoutAddressController<DefaultCheckoutAddressFormData> {

    private final CartReverseRouter cartReverseRouter;
    private final CheckoutReverseRouter checkoutReverseRouter;

    @Inject
    public CheckoutAddressController(final CartFinder cartFinder,
                                     final CheckoutAddressExecutor checkoutAddressExecutor,
                                     final CheckoutAddressPageContentFactory checkoutAddressPageContentFactory,
                                     final CartReverseRouter cartReverseRouter,
                                     final CheckoutReverseRouter checkoutReverseRouter) {
        super(cartFinder, checkoutAddressExecutor, checkoutAddressPageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
        this.checkoutReverseRouter = checkoutReverseRouter;
    }

    @Override
    public Class<DefaultCheckoutAddressFormData> getFormDataClass() {
        return DefaultCheckoutAddressFormData.class;
    }

    @Override
    protected CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.showCart());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final DefaultCheckoutAddressFormData formData, final Cart oldCart, final Cart updatedCart) {
        return redirectTo(checkoutReverseRouter.checkoutShippingPageCall());
    }
}