package controllers.shoppingcart;

import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.framework.checkout.shipping.CheckoutShippingControllerAction;
import com.commercetools.sunrise.framework.checkout.shipping.CheckoutShippingFormData;
import com.commercetools.sunrise.framework.checkout.shipping.ShippingSettings;
import com.commercetools.sunrise.framework.checkout.shipping.SunriseCheckoutShippingController;
import com.commercetools.sunrise.framework.checkout.shipping.viewmodels.CheckoutShippingPageContentFactory;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
import com.commercetools.sunrise.sessions.cart.CartShippingInfoExpansionControllerComponent;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        TemplateControllerComponentsSupplier.class,
        CheckoutStepControllerComponent.class,
        CartOperationsControllerComponentSupplier.class,
        CartShippingInfoExpansionControllerComponent.class
})
public final class CheckoutShippingController extends SunriseCheckoutShippingController {

    private final CartReverseRouter cartReverseRouter;
    private final CheckoutReverseRouter checkoutReverseRouter;

    @Inject
    public CheckoutShippingController(final ContentRenderer contentRenderer,
                                      final FormFactory formFactory,
                                      final CheckoutShippingFormData formData,
                                      final CartFinder cartFinder,
                                      final CheckoutShippingControllerAction controllerAction,
                                      final CheckoutShippingPageContentFactory pageContentFactory,
                                      final ShippingSettings shippingSettings,
                                      final CartReverseRouter cartReverseRouter,
                                      final CheckoutReverseRouter checkoutReverseRouter) {
        super(contentRenderer, formFactory, formData, cartFinder, controllerAction, pageContentFactory, shippingSettings);
        this.cartReverseRouter = cartReverseRouter;
        this.checkoutReverseRouter = checkoutReverseRouter;
    }

    @Nullable
    @Override
    public String getTemplateName() {
        return "checkout-shipping";
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectToCall(cartReverseRouter.cartDetailPageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final CheckoutShippingFormData formData) {
        return redirectToCall(checkoutReverseRouter.checkoutPaymentPageCall());
    }
}
