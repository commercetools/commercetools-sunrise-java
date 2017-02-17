package demo.shoppingcart;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.reverserouter.CartReverseRouter;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.hooks.ComponentRegistry;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.CheckoutShippingExecutor;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.DefaultCheckoutShippingFormData;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.ShippingSettings;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.SunriseCheckoutShippingController;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.view.CheckoutShippingPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
public final class CheckoutShippingController extends SunriseCheckoutShippingController<DefaultCheckoutShippingFormData> {

    private final CartReverseRouter cartReverseRouter;
    private final CheckoutReverseRouter checkoutReverseRouter;

    @Inject
    public CheckoutShippingController(final ComponentRegistry componentRegistry,
                                      final TemplateRenderer templateRenderer,
                                      final FormFactory formFactory,
                                      final CartFinder cartFinder,
                                      final CheckoutShippingExecutor checkoutShippingExecutor,
                                      final CheckoutShippingPageContentFactory checkoutShippingPageContentFactory,
                                      final ShippingSettings shippingSettings,
                                      final CartReverseRouter cartReverseRouter,
                                      final CheckoutReverseRouter checkoutReverseRouter) {
        super(componentRegistry, templateRenderer, formFactory, cartFinder, checkoutShippingExecutor, checkoutShippingPageContentFactory, shippingSettings);
        this.cartReverseRouter = cartReverseRouter;
        this.checkoutReverseRouter = checkoutReverseRouter;
    }

    @Inject
    public void registerComponents(final CheckoutStepControllerComponent checkoutStepControllerComponent) {
        register(checkoutStepControllerComponent);
    }

    @Override
    public Class<DefaultCheckoutShippingFormData> getFormDataClass() {
        return DefaultCheckoutShippingFormData.class;
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.showCart());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final DefaultCheckoutShippingFormData formData) {
        return redirectTo(checkoutReverseRouter.checkoutPaymentPageCall());
    }
}
