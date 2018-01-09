package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.core.controllers.SunriseContentFormController;
import com.commercetools.sunrise.core.controllers.WithContentFormFlow;
import com.commercetools.sunrise.core.hooks.EnableHooks;
import com.commercetools.sunrise.core.renderers.ContentRenderer;
import com.commercetools.sunrise.core.reverserouters.SunriseRoute;
import com.commercetools.sunrise.core.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import com.commercetools.sunrise.shoppingcart.checkout.payment.viewmodels.CheckoutPaymentPageContentFactory;
import io.sphere.sdk.carts.Cart;
import play.data.Form;
import play.data.FormFactory;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public abstract class SunriseCheckoutPaymentController extends SunriseContentFormController implements WithContentFormFlow<Void, Cart, CheckoutPaymentFormData> {

    private final CheckoutPaymentFormData formData;
    private final CheckoutPaymentControllerAction controllerAction;
    private final CheckoutPaymentPageContentFactory pageContentFactory;

    protected SunriseCheckoutPaymentController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                               final CheckoutPaymentFormData formData,
                                               final CheckoutPaymentControllerAction controllerAction,
                                               final CheckoutPaymentPageContentFactory pageContentFactory) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
    }

    @Override
    public final Class<? extends CheckoutPaymentFormData> getFormDataClass() {
        return formData.getClass();
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_PAYMENT_PAGE)
    public CompletionStage<Result> show() {
        return showFormPage(null, formData); // TODO it requires non-empty cart
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_PAYMENT_PROCESS)
    public CompletionStage<Result> process() {
        return processForm(null); // TODO it requires non-empty cart
    }

    @Override
    public CompletionStage<Cart> executeAction(final Void input, final CheckoutPaymentFormData formData) {
        return controllerAction.apply(formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final CheckoutPaymentFormData formData);

    @Override
    public PageContent createPageContent(final Void input, final Form<? extends CheckoutPaymentFormData> form) {
        return pageContentFactory.create(null, form);
    }

    @Override
    public void preFillFormData(final Void input, final CheckoutPaymentFormData formData) {
//        final String paymentMethodId = findPaymentMethodInfo(paymentMethodsWithCart.getCart())
//                .map(PaymentMethodInfo::getMethod)
//                .orElse(null);
//        formData.applyPaymentMethod(paymentMethodId);
    }

//    protected final Optional<PaymentMethodInfo> findPaymentMethodInfo(final Cart cart) {
//        return Optional.ofNullable(cart.getPaymentInfo())
//                .flatMap(info -> info.getPayments().stream()
//                        .map(Reference::getObj)
//                        .filter(Objects::nonNull)
//                        .map(Payment::getPaymentMethodInfo)
//                        .findAny());
//    }

//    @Override
//    public CompletionStage<Form<? extends CheckoutPaymentFormData>> validateForm(final Void input, final Form<? extends CheckoutPaymentFormData> filledForm) {
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
