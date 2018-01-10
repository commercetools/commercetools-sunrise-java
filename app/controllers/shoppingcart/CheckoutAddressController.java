package controllers.shoppingcart;

import com.commercetools.sunrise.core.components.RegisteredComponents;
import com.commercetools.sunrise.core.controllers.cache.NoCache;
import com.commercetools.sunrise.core.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.shoppingcart.checkout.CheckoutStepControllerComponent;
import com.commercetools.sunrise.shoppingcart.checkout.address.CheckoutAddressControllerAction;
import com.commercetools.sunrise.shoppingcart.checkout.address.CheckoutAddressFormData;
import com.commercetools.sunrise.shoppingcart.checkout.address.SunriseCheckoutAddressController;
import com.commercetools.sunrise.shoppingcart.checkout.address.viewmodels.CheckoutAddressPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.FormFactory;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@RegisteredComponents(CheckoutStepControllerComponent.class)
public final class CheckoutAddressController extends SunriseCheckoutAddressController {

    @Inject
    public CheckoutAddressController(final ContentRenderer contentRenderer,
                                     final FormFactory formFactory,
                                     final CheckoutAddressFormData formData,
                                     final CheckoutAddressControllerAction controllerAction,
                                     final CheckoutAddressPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory, formData, controllerAction, pageContentFactory);
    }

    @Override
    public String getTemplateName() {
        return "checkout-address";
    }

    @Override
    public String getCmsPageKey() {
        return "default";
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final CheckoutAddressFormData formData) {
        return redirectAsync(routes.CheckoutAddressController.show());
    }
}