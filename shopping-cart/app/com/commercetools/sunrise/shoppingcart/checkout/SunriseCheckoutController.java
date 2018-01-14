package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCheckoutController extends SunriseController {

    private final TemplateEngine templateEngine;
    private final SetAddressFormAction setAddressFormAction;
    private final SetShippingFormAction setShippingFormAction;
    private final SetPaymentFormAction setPaymentFormAction;
    private final PlaceOrderFormAction placeOrderFormAction;

    protected SunriseCheckoutController(final TemplateEngine templateEngine,
                                        final SetAddressFormAction setAddressFormAction,
                                        final SetShippingFormAction setShippingFormAction,
                                        final SetPaymentFormAction setPaymentFormAction,
                                        final PlaceOrderFormAction placeOrderFormAction) {
        this.templateEngine = templateEngine;
        this.setAddressFormAction = setAddressFormAction;
        this.setShippingFormAction = setShippingFormAction;
        this.setPaymentFormAction = setPaymentFormAction;
        this.placeOrderFormAction = placeOrderFormAction;
    }

    @EnableHooks
    public CompletionStage<Result> showSetAddressForm() {
        return templateEngine.render("checkout-address")
                .thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> setAddress() {
        return setAddressFormAction.apply(this::onAddressSet,
                form -> {
                    final PageData pageData = PageData.of().put("checkoutAddressForm", form);
                    return templateEngine.render("checkout-address", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    public CompletionStage<Result> showSetShippingForm() {
        return templateEngine.render("checkout-shipping")
                .thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> setShipping() {
        return setShippingFormAction.apply(this::onShippingSet,
                form -> {
                    final PageData pageData = PageData.of().put("checkoutShippingForm", form);
                    return templateEngine.render("checkout-shipping", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    public CompletionStage<Result> showSetPaymentForm() {
        return templateEngine.render("checkout-payment")
                .thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> setPayment() {
        return setPaymentFormAction.apply(this::onPaymentSet,
                form -> {
                    final PageData pageData = PageData.of().put("checkoutPaymentForm", form);
                    return templateEngine.render("checkout-payment", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    public CompletionStage<Result> showPlaceOrderForm() {
        return templateEngine.render("checkout-confirmation")
                .thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> placeOrder() {
        return placeOrderFormAction.apply(this::onOrderPlaced,
                form -> {
                    final PageData pageData = PageData.of().put("checkoutPlaceOrderForm", form);
                    return templateEngine.render("checkout-confirmation", pageData)
                            .thenApply(Results::badRequest);
                });
    }

    @EnableHooks
    public CompletionStage<Result> showOrder() {
        final String orderNumber = Http.Context.current().flash().get("orderNumber");
        final PageData pageData = PageData.of().put("orderNumber", orderNumber);
        return templateEngine.render("checkout-thankyou", pageData)
                .thenApply(Results::ok);
    }

    protected abstract Result onAddressSet();

    protected abstract Result onShippingSet();

    protected abstract Result onPaymentSet();

    protected abstract Result onOrderPlaced();


//    @Override
//    public void preFillFormData(final Void input, final SetShippingFormData formData) {
//        final String shippingMethodId = findShippingMethodId(shippingMethodsWithCart.getCart()).orElse(null);
//        formData.applyShippingMethod(shippingMethodId);
//    }
//
//    protected final Optional<String> findShippingMethodId(final Cart cart) {
//        return Optional.ofNullable(cart.getShippingInfo())
//                .flatMap(info -> Optional.ofNullable(info.getShippingMethod()).map(Reference::getId));
//    }

    //    @Override
//    public void preFillFormData(final Void input, final SetPaymentFormData formData) {
//        final String paymentMethodId = findPaymentMethodInfo(paymentMethodsWithCart.getCart())
//                .map(PaymentMethodInfo::getMethod)
//                .orElse(null);
//        formData.applyPaymentMethod(paymentMethodId);
//    }

//    protected final Optional<PaymentMethodInfo> findPaymentMethodInfo(final Cart cart) {
//        return Optional.ofNullable(cart.getPaymentInfo())
//                .flatMap(info -> info.getPayments().stream()
//                        .map(Reference::getObj)
//                        .filter(Objects::nonNull)
//                        .map(Payment::getPaymentMethodInfo)
//                        .findAny());
//    }

//    @Override
//    public CompletionStage<Form<? extends SetPaymentFormData>> validateForm(final Void input, final Form<? extends SetPaymentFormData> filledForm) {
//        final String selectedPaymentMethod = filledForm.field("payment").valueOr("");
//        if (!selectedPaymentMethod.isEmpty() && !isValidPaymentMethod(paymentMethodsWithCart, selectedPaymentMethod)) {
//            filledForm.reject("Invalid payment error"); // TODO get from i18n
//        }
//        return completedFuture(filledForm);
//    }

//    private boolean isValidPaymentMethod(final PaymentMethodsWithCart paymentMethodsWithCart, final String method) {
//        return paymentMethodsWithCart.getPaymentMethods().stream()
//                .anyMatch(paymentMethod -> Objects.equals(paymentMethod.getMethod(), method));
//    }
}