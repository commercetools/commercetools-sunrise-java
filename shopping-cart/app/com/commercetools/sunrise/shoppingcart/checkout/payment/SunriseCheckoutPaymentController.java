package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.framework.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.framework.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.framework.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.reverserouters.SunriseRoute;
import com.commercetools.sunrise.framework.hooks.RunRequestStartedHook;
import com.commercetools.sunrise.framework.reverserouters.shoppingcart.CheckoutReverseRouter;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.checkout.payment.view.CheckoutPaymentPageContentFactory;
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

public abstract class SunriseCheckoutPaymentController<F extends CheckoutPaymentFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, PaymentMethodsWithCart, Cart>, WithRequiredCart {

    private final CartFinder cartFinder;
    private final CheckoutPaymentControllerAction checkoutPaymentControllerAction;
    private final CheckoutPaymentPageContentFactory checkoutPaymentPageContentFactory;
    private final PaymentSettings paymentSettings;

    protected SunriseCheckoutPaymentController(final TemplateRenderer templateRenderer, final FormFactory formFactory,
                                               final CartFinder cartFinder,
                                               final CheckoutPaymentControllerAction checkoutPaymentControllerAction,
                                               final CheckoutPaymentPageContentFactory checkoutPaymentPageContentFactory,
                                               final PaymentSettings paymentSettings) {
        super(templateRenderer, formFactory);
        this.cartFinder = cartFinder;
        this.checkoutPaymentControllerAction = checkoutPaymentControllerAction;
        this.checkoutPaymentPageContentFactory = checkoutPaymentPageContentFactory;
        this.paymentSettings = paymentSettings;
    }

    @Override
    public CartFinder getCartFinder() {
        return cartFinder;
    }

    @RunRequestStartedHook
    @SunriseRoute(CheckoutReverseRouter.CHECKOUT_PAYMENT_PAGE)
    public CompletionStage<Result> show(final String languageTag) {
        return requireNonEmptyCart(cart ->
                findPaymentMethods(cart, paymentMethods ->
                        showFormPage(PaymentMethodsWithCart.of(paymentMethods, cart))));
    }

    @RunRequestStartedHook
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
    public CompletionStage<Cart> executeAction(final PaymentMethodsWithCart paymentMethodsWithCart, final F formData) {
        return checkoutPaymentControllerAction.apply(paymentMethodsWithCart, formData);
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final Cart updatedCart, final F formData);

    @Override
    public PageContent createPageContent(final PaymentMethodsWithCart paymentMethodsWithCart, final Form<F> form) {
        return checkoutPaymentPageContentFactory.create(paymentMethodsWithCart, form);
    }

    @Override
    public void preFillFormData(final PaymentMethodsWithCart paymentMethodsWithCart, final F formData) {
        final String paymentMethodId = findPaymentMethodInfo(paymentMethodsWithCart.getCart())
                .map(PaymentMethodInfo::getMethod)
                .orElse(null);
        formData.setPayment(paymentMethodId);
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
    public CompletionStage<Form<F>> validateForm(final PaymentMethodsWithCart paymentMethodsWithCart, final Form<F> filledForm) {
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
