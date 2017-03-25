package com.commercetools.sunrise.framework.checkout.payment;

import com.commercetools.sunrise.framework.viewmodels.content.PageContent;
import com.commercetools.sunrise.framework.CartFinder;
import com.commercetools.sunrise.framework.WithRequiredCart;
import com.commercetools.sunrise.framework.checkout.payment.viewmodels.CheckoutPaymentPageContentFactory;
import com.commercetools.sunrise.framework.controllers.SunriseContentFormController;
import com.commercetools.sunrise.framework.controllers.WithContentFormFlow;
import com.commercetools.sunrise.framework.hooks.EnableHooks;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.checkout.CheckoutReverseRouter;
import com.commercetools.sunrise.framework.template.engine.ContentRenderer;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class SunriseCheckoutPaymentController extends SunriseContentFormController
        implements WithContentFormFlow<PaymentMethodsWithCart, Cart, CheckoutPaymentFormData>, WithRequiredCart {

    private final CheckoutPaymentFormData formData;
    private final CartFinder cartFinder;
    private final CheckoutPaymentControllerAction controllerAction;
    private final CheckoutPaymentPageContentFactory pageContentFactory;
    private final PaymentSettings paymentSettings;

    protected SunriseCheckoutPaymentController(final ContentRenderer contentRenderer, final FormFactory formFactory,
                                               final CheckoutPaymentFormData formData, final CartFinder cartFinder,
                                               final CheckoutPaymentControllerAction controllerAction,
                                               final CheckoutPaymentPageContentFactory pageContentFactory,
                                               final PaymentSettings paymentSettings) {
        super(contentRenderer, formFactory);
        this.formData = formData;
        this.cartFinder = cartFinder;
        this.controllerAction = controllerAction;
        this.pageContentFactory = pageContentFactory;
        this.paymentSettings = paymentSettings;
    }

    @Override
    public final Class<? extends CheckoutPaymentFormData> getFormDataClass() {
        return formData.getClass();
    }

    @Override
    public final CartFinder getCartFinder() {
        return cartFinder;
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_PAYMENT_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireNonEmptyCart(cart ->
                findPaymentMethods(cart, paymentMethods ->
                        showFormPage(PaymentMethodsWithCart.of(paymentMethods, cart), formData)));
    }

    @EnableHooks
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_PAYMENT_PROCESS)
    public CompletionStage<Result> process(final String languageTag) {
        return requireNonEmptyCart(cart ->
                findPaymentMethods(cart, paymentMethods ->
                        processForm(PaymentMethodsWithCart.of(paymentMethods, cart))));
    }

    protected final CompletionStage<Result> findPaymentMethods(final Cart cart, final Function<List<PaymentMethodInfo>, CompletionStage<Result>> nextAction) {
        return paymentSettings.getPaymentMethods(cart)
                .thenComposeAsync(nextAction, HttpExecution.defaultContext());
    }

    @Override
    public CompletionStage<Cart> executeAction(final PaymentMethodsWithCart paymentMethodsWithCart, final CheckoutPaymentFormData formData) {
        return controllerAction.apply(paymentMethodsWithCart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final CheckoutPaymentFormData formData);

    @Override
    public PageContent createPageContent(final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> form) {
        return pageContentFactory.create(paymentMethodsWithCart, form);
    }

    @Override
    public void preFillFormData(final PaymentMethodsWithCart paymentMethodsWithCart, final CheckoutPaymentFormData formData) {
        final String paymentMethodId = findPaymentMethodInfo(paymentMethodsWithCart.getCart())
                .map(PaymentMethodInfo::getMethod)
                .orElse(null);
        formData.applyPaymentMethod(paymentMethodId);
    }

    protected final Optional<PaymentMethodInfo> findPaymentMethodInfo(final Cart cart) {
        return Optional.ofNullable(cart.getPaymentInfo())
                .flatMap(info -> info.getPayments().stream()
                        .map(Reference::getObj)
                        .filter(Objects::nonNull)
                        .map(Payment::getPaymentMethodInfo)
                        .findAny());
    }

    @Override
    public CompletionStage<Form<? extends CheckoutPaymentFormData>> validateForm(final PaymentMethodsWithCart paymentMethodsWithCart, final Form<? extends CheckoutPaymentFormData> filledForm) {
        final String selectedPaymentMethod = filledForm.field("payment").valueOr("");
        if (!selectedPaymentMethod.isEmpty() && !isValidPaymentMethod(paymentMethodsWithCart, selectedPaymentMethod)) {
            filledForm.reject("Invalid payment error"); // TODO get from i18n
        }
        return completedFuture(filledForm);
    }

    private boolean isValidPaymentMethod(final PaymentMethodsWithCart paymentMethodsWithCart, final String method) {
        return paymentMethodsWithCart.getPaymentMethods().stream()
                .anyMatch(paymentMethod -> Objects.equals(paymentMethod.getMethod(), method));
    }
}
