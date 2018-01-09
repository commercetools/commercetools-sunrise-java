package com.commercetools.sunrise.shoppingcart.checkout.shipping;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentFormFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.shoppingcart.checkout.shipping.viewmodels.CheckoutShippingPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCheckoutShippingController extends SunriseContentFormController implements WithContentFormFlow<Void, Cart, CheckoutShippingFormData> {

    private final CheckoutShippingFormData formData;
    private final CheckoutShippingControllerAction controllerAction;
    private final CheckoutShippingPageContentFactory pageContentFactory;

    protected SunriseCheckoutShippingController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                                final CheckoutShippingFormData formData,
                                                final CheckoutShippingControllerAction controllerAction,
                                                final CheckoutShippingPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public final Class<? extends CheckoutShippingFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_SHIPPING_PAGE)
    public CompletionStage<Result> show() {
        return showFormPage(null, formData); // TODO it requires non-empty
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_SHIPPING_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null); // TODO it requires non-empty
    }

    @Override
    public CompletionStage<Cart> executeAction(final Void input, final CheckoutShippingFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final CheckoutShippingFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends CheckoutShippingFormData> form) {
        return pageContentFactory.create(null, form);
    }

    @Override
    public void preFillFormData(final Void input, final CheckoutShippingFormData formData) {
//        final String shippingMethodId = findShippingMethodId(shippingMethodsWithCart.getCart()).orElse(null);
//        formData.applyShippingMethod(shippingMethodId);
    }

//    protected final Optional<String> findShippingMethodId(final Cart cart) {
//        return Optional.ofNullable(cart.getShippingInfo())
//                .flatMap(info -> Optional.ofNullable(info.getShippingMethod()).map(Reference::getId));
//    }
}