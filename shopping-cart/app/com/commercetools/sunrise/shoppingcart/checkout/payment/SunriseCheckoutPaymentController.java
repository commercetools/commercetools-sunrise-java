package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.controllers.WithFormFlow;
import com.commercetools.sunrise.common.controllers.WithTemplateName;
import com.commercetools.sunrise.framework.annotations.IntroducingMultiControllerComponents;
import com.commercetools.sunrise.framework.annotations.SunriseRoute;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.checkout.payment.view.CheckoutPaymentPageContent;
import com.commercetools.sunrise.shoppingcart.checkout.payment.view.CheckoutPaymentPageContentFactory;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkShoppingCartController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentMethodInfo;
import play.data.Form;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static org.apache.commons.lang3.ObjectUtils.firstNonNull;
import static play.libs.concurrent.HttpExecution.defaultContext;

@IntroducingMultiControllerComponents(CheckoutPaymentThemeLinksControllerComponent.class)
public abstract class SunriseCheckoutPaymentController<F extends CheckoutPaymentFormData> extends SunriseFrameworkShoppingCartController implements WithTemplateName, WithFormFlow<F, Cart, Cart> {

    private final CheckoutPaymentExecutor checkoutPaymentExecutor;
    private final CheckoutPaymentPageContentFactory checkoutPaymentPageContentFactory;
    private final PaymentSettings paymentSettings;

    protected SunriseCheckoutPaymentController(final CartFinder cartFinder, final CheckoutPaymentExecutor checkoutPaymentExecutor,
                                               final CheckoutPaymentPageContentFactory checkoutPaymentPageContentFactory,
                                               final PaymentSettings paymentSettings) {
        super(cartFinder);
        this.checkoutPaymentExecutor = checkoutPaymentExecutor;
        this.checkoutPaymentPageContentFactory = checkoutPaymentPageContentFactory;
        this.paymentSettings = paymentSettings;
    }

    @Override
    public Set<String> getFrameworkTags() {
        final Set<String> frameworkTags = super.getFrameworkTags();
        frameworkTags.addAll(asList("checkout", "checkout-payment"));
        return frameworkTags;
    }

    @Override
    public String getTemplateName() {
        return "checkout-payment";
    }

    @SunriseRoute("checkoutPaymentPageCall")
    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::showFormPage));
    }

    @SunriseRoute("checkoutPaymentProcessFormCall")
    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> requireNonEmptyCart(this::processForm));
    }

    @Override
    public CompletionStage<Cart> doAction(final F formData, final Cart cart) {
        return checkoutPaymentExecutor.apply(cart, formData);
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<F> form, final Cart cart, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException);
        return asyncBadRequest(renderPage(form, cart, null));
    }

    @Override
    public abstract CompletionStage<Result> handleSuccessfulAction(final F formData, final Cart oldCart, final Cart updatedCart);

    @Override
    public CompletionStage<Html> renderPage(final Form<F> form, final Cart oldCart, @Nullable final Cart updatedCart) {
        final Cart cart = firstNonNull(updatedCart, oldCart);
        return paymentSettings.getPaymentMethods(cart)
                .thenComposeAsync(paymentMethods -> {
                    final PaymentMethodsWithCart paymentMethodsWithCart = new PaymentMethodsWithCart(paymentMethods, cart);
                    final CheckoutPaymentPageContent pageContent = checkoutPaymentPageContentFactory.create(paymentMethodsWithCart, form);
                    return renderPageWithTemplate(pageContent, getTemplateName());
                }, defaultContext());
    }

    @Override
    public void preFillFormData(final F formData, final Cart cart) {
        final String paymentMethodId = findPaymentMethodInfo(cart)
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
    public CompletionStage<Form<F>> validateForm(final Cart cart, final Form<F> filledForm) {
        final String selectedPaymentMethod = filledForm.field("payment").valueOr("");
        if (!selectedPaymentMethod.isEmpty()) {
            isValidPaymentMethod(cart, selectedPaymentMethod).thenAcceptAsync(isValid -> {
                if (!isValid) {
                    filledForm.reject("Invalid payment error"); // TODO get from i18n
                }
            }, HttpExecution.defaultContext());
        }
        return completedFuture(filledForm);
    }

    private CompletionStage<Boolean> isValidPaymentMethod(final Cart cart, final String method) {
        return paymentSettings.getPaymentMethods(cart)
                .thenApply(paymentMethods -> paymentMethods.stream()
                        .anyMatch(paymentMethod -> Objects.equals(paymentMethod.getMethod(), method)));
    }
}
