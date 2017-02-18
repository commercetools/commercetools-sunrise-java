package controllers.shoppingcart;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CartReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CheckoutReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
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
@RegisteredComponents({
        CheckoutStepControllerComponent.class
})
public final class CheckoutShippingController extends SunriseCheckoutShippingController<DefaultCheckoutShippingFormData> {

    private final CartReverseRouter cartReverseRouter;
    private final CheckoutReverseRouter checkoutReverseRouter;

    @Inject
    public CheckoutShippingController(final TemplateRenderer templateRenderer,
                                      final FormFactory formFactory,
                                      final CartFinder cartFinder,
                                      final CheckoutShippingExecutor checkoutShippingExecutor,
                                      final CheckoutShippingPageContentFactory checkoutShippingPageContentFactory,
                                      final ShippingSettings shippingSettings,
                                      final CartReverseRouter cartReverseRouter,
                                      final CheckoutReverseRouter checkoutReverseRouter) {
        super(templateRenderer, formFactory, cartFinder, checkoutShippingExecutor, checkoutShippingPageContentFactory, shippingSettings);
        this.cartReverseRouter = cartReverseRouter;
        this.checkoutReverseRouter = checkoutReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "checkout-shipping";
    }

    @Override
    public Class<DefaultCheckoutShippingFormData> getFormDataClass() {
        return DefaultCheckoutShippingFormData.class;
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.cartDetailPageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final DefaultCheckoutShippingFormData formData) {
        return redirectTo(checkoutReverseRouter.checkoutPaymentPageCall());
    }
}
