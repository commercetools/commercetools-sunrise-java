package controllers;

import com.commercetools.sunrise.controllers.cache.NoCache;
import com.commercetools.sunrise.controllers.metrics.LogMetrics;
import com.commercetools.sunrise.core.SunriseController;
import com.commercetools.sunrise.core.components.DisableComponents;
import com.commercetools.sunrise.core.components.EnableComponents;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.i18n.LanguageSelectorComponent;
import com.commercetools.sunrise.core.localization.CountrySelectorComponent;
import com.commercetools.sunrise.core.renderers.TemplateEngine;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.carts.CartPaymentComponent;
import com.commercetools.sunrise.models.carts.CartShippingComponent;
import com.commercetools.sunrise.models.categories.CategoryNavigationComponent;
import com.commercetools.sunrise.models.orders.OrderCreatedComponent;
import com.commercetools.sunrise.shoppingcart.checkout.PlaceOrderFormAction;
import com.commercetools.sunrise.shoppingcart.checkout.SetAddressFormAction;
import com.commercetools.sunrise.shoppingcart.checkout.SetPaymentFormAction;
import com.commercetools.sunrise.shoppingcart.checkout.SetShippingFormAction;
import play.mvc.Result;
import play.mvc.Results;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

@LogMetrics
@NoCache
@DisableComponents({
        CountrySelectorComponent.class,
        LanguageSelectorComponent.class,
        CategoryNavigationComponent.class
})
@EnableComponents({
        CartShippingComponent.class,
        CartPaymentComponent.class,
        OrderCreatedComponent.class
})
public class CheckoutController extends SunriseController {

    private static final String ADDRESS_TEMPLATE = "checkout-address";
    private static final String SHIPPING_TEMPLATE = "checkout-shipping";
    private static final String PAYMENT_TEMPLATE = "checkout-payment";
    private static final String PLACE_ORDER_TEMPLATE = "checkout-confirmation";
    private static final String THANKYOU_TEMPLATE = "checkout-thankyou";

    private final TemplateEngine templateEngine;
    private final SetAddressFormAction setAddressFormAction;
    private final SetShippingFormAction setShippingFormAction;
    private final SetPaymentFormAction setPaymentFormAction;
    private final PlaceOrderFormAction placeOrderFormAction;

    @Inject
    CheckoutController(final TemplateEngine templateEngine,
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
        return templateEngine.render(ADDRESS_TEMPLATE).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> setAddress() {
        return setAddressFormAction.apply(
                () -> routes.CheckoutController.showSetShippingForm(),
                form -> templateEngine.render(ADDRESS_TEMPLATE, PageData.of().put("checkoutAddressForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> showSetShippingForm() {
        return templateEngine.render(SHIPPING_TEMPLATE).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> setShipping() {
        return setShippingFormAction.apply(
                () -> routes.CheckoutController.showSetPaymentForm(),
                form -> templateEngine.render(SHIPPING_TEMPLATE, PageData.of().put("checkoutShippingForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> showSetPaymentForm() {
        return templateEngine.render(PAYMENT_TEMPLATE).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> setPayment() {
        return setPaymentFormAction.apply(
                () -> routes.CheckoutController.showPlaceOrderForm(),
                form -> templateEngine.render(PAYMENT_TEMPLATE, PageData.of().put("checkoutPaymentForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> showPlaceOrderForm() {
        return templateEngine.render(PLACE_ORDER_TEMPLATE).thenApply(Results::ok);
    }

    @EnableHooks
    public CompletionStage<Result> placeOrder() {
        return placeOrderFormAction.apply(
                () -> routes.CheckoutController.showOrder(),
                form -> templateEngine.render(PLACE_ORDER_TEMPLATE, PageData.of().put("checkoutPlaceOrderForm", form)));
    }

    @EnableHooks
    public CompletionStage<Result> showOrder() {
        return templateEngine.render(THANKYOU_TEMPLATE).thenApply(Results::ok);
    }

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