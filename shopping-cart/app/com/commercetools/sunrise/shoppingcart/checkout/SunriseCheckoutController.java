package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.core.controllers.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.data.FormFactory;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCheckoutController extends SunriseController {

    private final TemplateEngine templateEngine;
    private final CheckoutAddressFormAction checkoutAddressFormAction;
    private final CheckoutShippingFormAction checkoutShippingFormAction;

    protected SunriseCheckoutController(final ContentRenderer contentRenderer,
                                        final FormFactory formFactory, final CheckoutAddressFormData formData,
                                        final CheckoutAddressFormAction controllerAction) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_ADDRESS_PAGE)
    public CompletionStage<Result> showSetAddressForm() {
        return templateEngine.render("checkout-address")
                .thenApply(Results::ok);
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_ADDRESS_PROCESS)
    public CompletionStage<Result> setAddress() {
        return checkoutAddressFormAction.apply(this::onAddressSet,
                form -> {
                    final PageData pageData = PageData.of().put("checkoutAddressForm", form);
                    return templateEngine.render("checkout-address", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_ADDRESS_PAGE)
    public CompletionStage<Result> showSetShippingForm() {
        return templateEngine.render("checkout-shipping")
                .thenApply(Results::ok);
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_ADDRESS_PROCESS)
    public CompletionStage<Result> setShipping() {
        return checkoutShippingFormAction.apply(this::onShippingSet,
                form -> {
                    final PageData pageData = PageData.of().put("checkoutShippingForm", form);
                    return templateEngine.render("checkout-shipping", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    protected abstract Result onAddressSet();

    protected abstract Result onShippingSet();

//    @Override
//    public void preFillFormData(final Void input, final CheckoutShippingFormData formData) {
//        final String shippingMethodId = findShippingMethodId(shippingMethodsWithCart.getCart()).orElse(null);
//        formData.applyShippingMethod(shippingMethodId);
//    }
//
//    protected final Optional<String> findShippingMethodId(final Cart cart) {
//        return Optional.ofNullable(cart.getShippingInfo())
//                .flatMap(info -> Optional.ofNullable(info.getShippingMethod()).map(Reference::getId));
//    }
}