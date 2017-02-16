package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.controllers.SunriseTemplateFormController;
import com.commercetools.sunrise.common.controllers.WithTemplateFormFlow;
import com.commercetools.sunrise.common.pages.PageContent;
import com.commercetools.sunrise.common.template.engine.TemplateRenderer;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.WithRequiredCart;
import com.commercetools.sunrise.shoppingcart.checkout.payment.view.CheckoutPaymentPageContentFactory;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.data.Form;
import play.data.FormFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@IntroducingMultiControllerComponents(CheckoutPaymentThemeLinksControllerComponent.class)
public abstract class SunriseCheckoutPaymentController<F extends CheckoutPaymentFormData> extends SunriseTemplateFormController implements WithTemplateFormFlow<F, PaymentMethodsWithCart, Cart>, WithRequiredCart {

    private final CartFinder cartFinder;
    private final CheckoutPaymentExecutor checkoutPaymentExecutor;
    private final CheckoutPaymentPageContentFactory checkoutPaymentPageContentFactory;
    private final PaymentSettings paymentSettings;

    protected SunriseCheckoutPaymentController(final RequestHookContext hookContext, final TemplateRenderer templateRenderer,
                                               final FormFactory formFactory, final CartFinder cartFinder,
                                               final CheckoutPaymentExecutor checkoutPaymentExecutor,
                                               final CheckoutPaymentPageContentFactory checkoutPaymentPageContentFactory,
                                               final PaymentSettings paymentSettings) {
        super(hookContext, templateRenderer, formFactory);
        this.cartFinder = cartFinder;
        this.checkoutPaymentExecutor = checkoutPaymentExecutor;
        this.checkoutPaymentPageContentFactory = checkoutPaymentPageContentFactory;
        this.paymentSettings = paymentSettings;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = new HashSet<>();
        frameworkTags.addAll(asList("checkout", "checkout-payment"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "checkout-payment";
    }

    @Override
    public CartFinder getCartFinder() {
        return cartFinder;
    }

    @SunriseRoute("checkoutPaymentPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requirePaymentMethodsWithCart(this::showFormPage));
    }

    @SunriseRoute("checkoutPaymentProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> requirePaymentMethodsWithCart(this::processForm));
    }

    @Override
    public CompletionStage<Cart> executeAction(final PaymentMethodsWithCart paymentMethodsWithCart, final F formData) {
        return checkoutPaymentExecutor.apply(paymentMethodsWithCart, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final PaymentMethodsWithCart paymentMethodsWithCart, final Form<F> form, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return showFormPageWithErrors(paymentMethodsWithCart, form);
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

    protected final CompletionStage<Result> requirePaymentMethodsWithCart(final Function<PaymentMethodsWithCart, CompletionStage<Result>> nextAction) {
        return requireNonEmptyCart(cart -> paymentSettings.getPaymentMethods(cart)
                .thenApply(paymentMethods -> PaymentMethodsWithCart.of(paymentMethods, cart))
                .thenComposeAsync(nextAction, HttpExecution.defaultContext()));
    }

    private boolean isValidPaymentMethod(final PaymentMethodsWithCart paymentMethodsWithCart, final String method) {
          return paymentMethodsWithCart.getPaymentMethods().stream()
                  .anyMatch(paymentMethod -> Objects.equals(paymentMethod.getMethod(), method));
    }
}
