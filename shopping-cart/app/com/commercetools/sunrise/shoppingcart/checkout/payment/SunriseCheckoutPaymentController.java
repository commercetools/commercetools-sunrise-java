package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.common.contexts.RequestScoped;
import com.commercetools.sunrise.common.controllers.SimpleFormBindingControllerTrait;
import com.commercetools.sunrise.common.controllers.WithOverwriteableTemplateName;
import com.commercetools.sunrise.common.reverserouter.CheckoutReverseRouter;
import com.commercetools.sunrise.payments.PaymentConfiguration;
import com.commercetools.sunrise.shoppingcart.common.SunriseFrameworkCartController;
import com.commercetools.sunrise.shoppingcart.common.WithCartPreconditions;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.PaymentInfo;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddPayment;
import io.sphere.sdk.carts.commands.updateactions.RemovePayment;
import io.sphere.sdk.client.ClientErrorException;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.models.Reference;
import io.sphere.sdk.payments.Payment;
import io.sphere.sdk.payments.PaymentDraft;
import io.sphere.sdk.payments.PaymentDraftBuilder;
import io.sphere.sdk.payments.PaymentMethodInfo;
import io.sphere.sdk.payments.commands.PaymentCreateCommand;
import io.sphere.sdk.payments.commands.PaymentDeleteCommand;
import io.sphere.sdk.payments.queries.PaymentByIdGet;
import io.sphere.sdk.utils.CompletableFutureUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.data.Form;
import play.libs.concurrent.HttpExecutionContext;
import play.mvc.Call;
import play.mvc.Result;
import play.twirl.api.Html;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static java.util.stream.Collectors.toList;
import static play.libs.concurrent.HttpExecution.defaultContext;

@RequestScoped
public abstract class SunriseCheckoutPaymentController extends SunriseFrameworkCartController
        implements WithOverwriteableTemplateName, SimpleFormBindingControllerTrait<CheckoutPaymentFormData, Cart, Cart>, WithCartPreconditions {

    private static final Logger logger = LoggerFactory.getLogger(SunriseCheckoutPaymentController.class);

    @Inject
    private PaymentConfiguration paymentConfiguration;
    @Inject
    private HttpExecutionContext httpExecutionContext;

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("checkout", "checkout-payment"));
    }

    @Override
    public String getTemplateName() {
        return "checkout-payment";
    }

    @Override
    public Class<? extends CheckoutPaymentFormData> getFormDataClass() {
        return DefaultCheckoutPaymentFormData.class;
    }

    public CompletionStage<Result> show(final String languageTag) {
        return doRequest(() -> loadCartWithPreconditions().thenComposeAsync(this::showForm, defaultContext()));
    }

    public CompletionStage<Result> process(final String languageTag) {
        return doRequest(() -> loadCartWithPreconditions().thenComposeAsync(this::validateForm, defaultContext()));
    }

    @Override
    public CompletionStage<? extends Cart> doAction(final CheckoutPaymentFormData formData, final Cart cart) {
        final String selectedPaymentMethod = formData.getPayment();
        return getPaymentMethodInfos()
                .thenComposeAsync(paymentMethods -> findSelectedPaymentMethod(paymentMethods, selectedPaymentMethod)
                        .map(paymentMethod -> setPaymentToCart(cart, paymentMethod))
                        .orElseThrow(() -> new RuntimeException("No valid payment method found")), // Should not happen after validation
                        httpExecutionContext.current());
    }

    @Override
    public CompletionStage<Result> handleClientErrorFailedAction(final Form<? extends CheckoutPaymentFormData> form, final Cart cart, final ClientErrorException clientErrorException) {
        saveUnexpectedFormError(form, clientErrorException, logger);
        return asyncBadRequest(renderPage(form, cart, null));
    }

    @Override
    public CompletionStage<Result> handleSuccessfulAction(final CheckoutPaymentFormData formData, final Cart context, final Cart result) {
        return redirectToCheckoutConfirmation();
    }

    @Override
    public CompletionStage<Html> renderPage(final Form<? extends CheckoutPaymentFormData> form, final Cart cart, @Nullable final Cart updatedCart) {
        return getPaymentMethodInfos()
                .thenComposeAsync(paymentMethods -> {
                    final Cart cartToRender = Optional.ofNullable(updatedCart).orElse(cart);
                    final CheckoutPaymentPageContentFactory pageContentFactory = injector().getInstance(CheckoutPaymentPageContentFactory.class);
                    final CheckoutPaymentPageContent pageContent = pageContentFactory.create(form, cartToRender, paymentMethods);
                    return renderPageWithTemplate(pageContent, getTemplateName());
                }, defaultContext());
    }

    @Override
    public void fillFormData(final CheckoutPaymentFormData formData, final Cart cart) {
        final String paymentMethodId = findPaymentMethodId(cart).orElse(null);
        formData.setPayment(paymentMethodId);
    }

    @Override
    public CompletionStage<Form<? extends CheckoutPaymentFormData>> asyncValidation(final Form<? extends CheckoutPaymentFormData> filledForm) {
        final String selectedPaymentMethod = filledForm.field("payment").valueOr("");
        if (!selectedPaymentMethod.isEmpty()) {
            return getPaymentMethodInfos()
                    .thenApply(paymentMethods -> {
                        final boolean isValidPaymentMethod = isValidPaymentMethod(paymentMethods, selectedPaymentMethod);
                        if (!isValidPaymentMethod) {
                            filledForm.reject("Invalid payment error"); // TODO get from i18n
                        }
                        return filledForm;
                    });
        } else {
            return completedFuture(filledForm);
        }
    }

    @Override
    public CompletionStage<Cart> loadCartWithPreconditions() {
        return requiringExistingPrimaryCartWithLineItem();
    }

    protected CompletionStage<Cart> setPaymentToCart(final Cart cart, final PaymentMethodInfo selectedPaymentMethod) {
        final List<PaymentMethodInfo> selectedPaymentMethods = singletonList(selectedPaymentMethod);
        return withPaymentsToRemove(cart, selectedPaymentMethods, paymentsToRemove ->
                withPaymentsToAdd(cart, selectedPaymentMethods, paymentsToAdd -> {
                    final Stream<RemovePayment> removePaymentStream = paymentsToRemove.stream().map(RemovePayment::of);
                    final Stream<AddPayment> addPaymentStream = paymentsToAdd.stream().map(AddPayment::of);
                    final List<UpdateAction<Cart>> updateActions = Stream.concat(removePaymentStream, addPaymentStream).collect(toList());
                    return sphere().execute(CartUpdateCommand.of(cart, updateActions));
                })
        );
    }

    protected CompletionStage<Cart> withPaymentsToRemove(final Cart cart, final List<PaymentMethodInfo> selectedPaymentMethods,
                                                         final Function<List<Payment>, CompletionStage<Cart>> setPaymentAction) {
        final List<Reference<Payment>> paymentRefs = Optional.ofNullable(cart.getPaymentInfo())
                .map(PaymentInfo::getPayments)
                .orElseGet(() -> {
                    logger.error("Payment info is not expanded in cart: the new payment information can be saved but the previous payments will not be removed.");
                    return emptyList();
                });
        final List<CompletionStage<Payment>> paymentStages = paymentRefs.stream()
                .map(paymentRef -> sphere().execute(PaymentByIdGet.of(paymentRef)))
                .collect(toList());
        return CompletableFutureUtils.listOfFuturesToFutureOfList(paymentStages)
                .thenComposeAsync(payments -> {
                    payments.removeIf(Objects::isNull);
                    final CompletionStage<Cart> updatedCartStage = setPaymentAction.apply(payments);
                    updatedCartStage.thenAccept(updatedCart ->
                            payments.forEach(payment -> sphere().execute(PaymentDeleteCommand.of(payment))));
                    return updatedCartStage;
                });
    }

    protected CompletionStage<Cart> withPaymentsToAdd(final Cart cart, final List<PaymentMethodInfo> selectedPaymentMethods,
                                                      final Function<List<Payment>, CompletionStage<Cart>> setPaymentAction) {
        final List<CompletionStage<Payment>> paymentStages = selectedPaymentMethods.stream()
                .map(selectedPaymentMethod -> {
                    final PaymentDraft paymentDraft = PaymentDraftBuilder.of(cart.getTotalPrice())
                            .paymentMethodInfo(selectedPaymentMethod)
                            .customer(Optional.ofNullable(cart.getCustomerId()).map(Customer::referenceOfId).orElse(null))
                            .build();
                    return sphere().execute(PaymentCreateCommand.of(paymentDraft));
                })
                .collect(toList());
        return CompletableFutureUtils.listOfFuturesToFutureOfList(paymentStages)
                .thenComposeAsync(payments -> {
                    payments.removeIf(Objects::isNull);
                    return setPaymentAction.apply(payments);
                });
    }

    protected CompletionStage<List<PaymentMethodInfo>> getPaymentMethodInfos() {
        return completedFuture(paymentConfiguration.getPaymentMethodInfoList());
    }

    protected boolean isValidPaymentMethod(final List<PaymentMethodInfo> paymentMethods, final String selectedPaymentMethod) {
        return findSelectedPaymentMethod(paymentMethods, selectedPaymentMethod).isPresent();
    }

    protected Optional<PaymentMethodInfo> findSelectedPaymentMethod(final List<PaymentMethodInfo> paymentMethods, final String selectedPaymentMethod) {
        return paymentMethods.stream()
                .filter(paymentMethod -> Objects.equals(paymentMethod.getMethod(), selectedPaymentMethod))
                .findAny();
    }

    protected final CompletionStage<Result> redirectToCheckoutConfirmation() {
        final Call call = injector().getInstance(CheckoutReverseRouter.class).checkoutConfirmationPageCall(userContext().languageTag());
        return completedFuture(redirect(call));
    }

    protected final Optional<String> findPaymentMethodId(final Cart cart) {
        return Optional.ofNullable(cart.getPaymentInfo())
                .flatMap(info -> info.getPayments().stream()
                        .map(Reference::getObj)
                        .filter(obj -> obj != null)
                        .map(obj -> obj.getPaymentMethodInfo().getMethod())
                        .findAny());
    }
}
