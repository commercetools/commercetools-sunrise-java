package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.shoppingcart.AbstractCartUpdateExecutor;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.PaymentInfo;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.AddPayment;
import io.sphere.sdk.carts.commands.updateactions.RemovePayment;
import io.sphere.sdk.client.SphereClient;
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
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

public class DefaultCheckoutPaymentExecutor extends AbstractCartUpdateExecutor implements CheckoutPaymentExecutor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutPaymentExecutor.class);

    private final PaymentSettings paymentSettings;

    @Inject
    protected DefaultCheckoutPaymentExecutor(final SphereClient sphereClient, final HookContext hookContext,
                                             final PaymentSettings paymentSettings) {
        super(sphereClient, hookContext);
        this.paymentSettings = paymentSettings;
    }

    @Override
    public CompletionStage<Cart> apply(final Cart cart, final CheckoutPaymentFormData formData) {
        final String selectedMethod = formData.getPayment();
        return findPaymentMethod(cart, selectedMethod)
                .thenComposeAsync(paymentMethodOpt -> paymentMethodOpt
                                .map(paymentMethod -> setPaymentToCart(cart, paymentMethod))
                                .orElseThrow(() -> new RuntimeException("No valid payment method found")), // Should not happen after validation
                        HttpExecution.defaultContext());
    }

    protected CompletionStage<Cart> setPaymentToCart(final Cart cart, final PaymentMethodInfo selectedPaymentMethod) {
        final List<PaymentMethodInfo> selectedPaymentMethods = singletonList(selectedPaymentMethod);
        return withPaymentsToRemove(cart, selectedPaymentMethods, paymentsToRemove ->
                withPaymentsToAdd(cart, selectedPaymentMethods, paymentsToAdd -> {
                    final Stream<RemovePayment> removePaymentStream = paymentsToRemove.stream().map(RemovePayment::of);
                    final Stream<AddPayment> addPaymentStream = paymentsToAdd.stream().map(AddPayment::of);
                    final List<UpdateAction<Cart>> updateActions = Stream.concat(removePaymentStream, addPaymentStream).collect(toList());
                    return getSphereClient().execute(CartUpdateCommand.of(cart, updateActions));
                })
        );
    }

    private CompletionStage<Optional<PaymentMethodInfo>> findPaymentMethod(final Cart cart, final String method) {
        return paymentSettings.getPaymentMethods(cart)
                .thenApplyAsync(paymentMethods -> paymentMethods.stream()
                                .filter(paymentMethod -> Objects.equals(paymentMethod.getMethod(), method))
                                .findAny(),
                        HttpExecution.defaultContext());
    }

    protected CompletionStage<Cart> withPaymentsToRemove(final Cart cart, final List<PaymentMethodInfo> selectedPaymentMethods,
                                                         final Function<List<Payment>, CompletionStage<Cart>> setPaymentAction) {
        final List<Reference<Payment>> paymentRefs = Optional.ofNullable(cart.getPaymentInfo())
                .map(PaymentInfo::getPayments)
                .orElseGet(() -> {
                    LOGGER.error("Payment info is not expanded in cart: the new payment information can be saved but the previous payments will not be removed.");
                    return emptyList();
                });
        final List<CompletionStage<Payment>> paymentStages = paymentRefs.stream()
                .map(paymentRef -> getSphereClient().execute(PaymentByIdGet.of(paymentRef)))
                .collect(toList());
        return CompletableFutureUtils.listOfFuturesToFutureOfList(paymentStages)
                .thenComposeAsync(payments -> {
                    payments.removeIf(Objects::isNull);
                    final CompletionStage<Cart> updatedCartStage = setPaymentAction.apply(payments);
                    updatedCartStage.thenAccept(updatedCart ->
                            payments.forEach(payment -> getSphereClient().execute(PaymentDeleteCommand.of(payment))));
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
                    return getSphereClient().execute(PaymentCreateCommand.of(paymentDraft));
                })
                .collect(toList());
        return CompletableFutureUtils.listOfFuturesToFutureOfList(paymentStages)
                .thenComposeAsync(payments -> {
                    payments.removeIf(Objects::isNull);
                    return setPaymentAction.apply(payments);
                });
    }
}
