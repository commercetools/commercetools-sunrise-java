package controllers.shoppingcart;

import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.framework.checkout.address.CheckoutAddressControllerAction;
import com.commercetools.sunrise.framework.checkout.address.CheckoutAddressFormData;
import com.commercetools.sunrise.framework.checkout.address.SunriseCheckoutAddressController;
import com.commercetools.sunrise.framework.checkout.address.viewmodels.CheckoutAddressPageContentFactory;
import com.commercetools.sunrise.framework.components.controllers.RegisteredComponents;
import com.commercetools.sunrise.framework.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.cart.CartReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import com.commercetools.sunrise.framework.template.TemplateControllerComponentsSupplier;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import com.commercetools.sunrise.sessions.cart.CartOperationsControllerComponentSupplier;
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
        CartOperationsControllerComponentSupplier.class
})
public final class CheckoutAddressController extends SunriseCheckoutAddressController {

    private final CartReverseRouter cartReverseRouter;
    private final CheckoutReverseRouter checkoutReverseRouter;

    @Inject
    public CheckoutAddressController(final ContentRenderer contentRenderer,
                                     final FormFactory formFactory,
                                     final CheckoutAddressFormData formData,
                                     final CartFinder cartFinder,
                                     final CheckoutAddressControllerAction controllerAction,
                                     final CheckoutAddressPageContentFactory pageContentFactory,
                                     final CartReverseRouter cartReverseRouter,
                                     final CheckoutReverseRouter checkoutReverseRouter) {
        super(contentRenderer, formFactory, formData, cartFinder, controllerAction, pageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
        this.checkoutReverseRouter = checkoutReverseRouter;
    }

    @Nullable
    @Override
    public String getTemplateName() {
        return "checkout-address";
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectToCall(cartReverseRouter.cartDetailPageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final CheckoutAddressFormData formData) {
        return redirectToCall(checkoutReverseRouter.checkoutShippingPageCall());
    }
}