package controllers.shoppingcart;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CartReverseRouter;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CheckoutReverseRouter;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.hooks.RegisteredComponents;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.shoppingcart.checkout.address.CheckoutAddressExecutor;
import com.commercetools.sunrise.shoppingcart.checkout.address.DefaultCheckoutAddressFormData;
import com.commercetools.sunrise.shoppingcart.checkout.address.SunriseCheckoutAddressController;
import com.commercetools.sunrise.shoppingcart.checkout.address.view.CheckoutAddressPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@NoCache
@RegisteredComponents({
        CheckoutStepControllerComponent.class
})
public final class CheckoutAddressController extends SunriseCheckoutAddressController<DefaultCheckoutAddressFormData> {

    private final CartReverseRouter cartReverseRouter;
    private final CheckoutReverseRouter checkoutReverseRouter;

    @Inject
    public CheckoutAddressController(final TemplateRenderer templateRenderer,
                                     final FormFactory formFactory,
                                     final CartFinder cartFinder,
                                     final CheckoutAddressExecutor checkoutAddressExecutor,
                                     final CheckoutAddressPageContentFactory checkoutAddressPageContentFactory,
                                     final CartReverseRouter cartReverseRouter,
                                     final CheckoutReverseRouter checkoutReverseRouter) {
        super(templateRenderer, formFactory, cartFinder, checkoutAddressExecutor, checkoutAddressPageContentFactory);
        this.cartReverseRouter = cartReverseRouter;
        this.checkoutReverseRouter = checkoutReverseRouter;
    }

    @Override
    public String getTemplateName() {
        return "checkout-address";
    }

    @Override
    public Class<DefaultCheckoutAddressFormData> getFormDataClass() {
        return DefaultCheckoutAddressFormData.class;
    }

    @Override
    public CompletionStage<Result> handleNotFoundCart() {
        return redirectTo(cartReverseRouter.cartDetailPageCall());
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final DefaultCheckoutAddressFormData formData) {
        return redirectTo(checkoutReverseRouter.checkoutShippingPageCall());
    }
}