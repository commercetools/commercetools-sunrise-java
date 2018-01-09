package com.commercetools.sunrise.shoppingcart.checkout.payment;

import com.commercetools.sunrise.models.carts.MyCartInCache;
import com.commercetools.sunrise.models.carts.MyCartUpdater;
import com.commercetools.sunrise.models.payments.PaymentSettings;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.PaymentInfo;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.stream.Collectors.toList;

/**
 * Deletes previous payments associated with the cart and adds the new one.
 * The {@link PaymentInfo#getPayments()} references should be expanded using the expansion path {@code paymentInfo.payments} to properly work, otherwise previous payments will not be deleted.
 */
final class DefaultCheckoutPaymentControllerAction implements CheckoutPaymentControllerAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutPaymentControllerAction.class);

    private final MyCartUpdater myCartUpdater;
    private final MyCartInCache myCartInCache;
    private final PaymentSettings paymentSettings;
    private final SphereClient sphereClient;

    @Inject
    DefaultCheckoutPaymentControllerAction(final MyCartUpdater myCartUpdater, final MyCartInCache myCartInCache,
                                           final PaymentSettings paymentSettings, final SphereClient sphereClient) {
        this.myCartUpdater = myCartUpdater;
        this.myCartInCache = myCartInCache;
        this.paymentSettings = paymentSettings;
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<Cart> apply(final CheckoutPaymentFormData formData) {
        return myCartInCache.require().thenComposeAsync(cart ->
                createPayment(cart, formData).thenComposeAsync(payment -> replacePayment(cart, payment), HttpExecution.defaultContext()),
                HttpExecution.defaultContext());
    }

    private CompletionStage<Cart> replacePayment(final Cart cart, final Payment payment) {
        final List<Reference<Payment>> paymentsToRemove = findPaymentsToRemove(cart);
        final CompletionStage<Cart> updatedCart = myCartUpdater.force(buildUpdateActions(payment, paymentsToRemove));
        updatedCart.thenRun(() -> deletePreviousPayments(paymentsToRemove));
        return updatedCart;
    }

    private List<UpdateAction<Cart>> buildUpdateActions(final Payment paymentToAdd, final List<Reference<Payment>> paymentsToRemove) {
        final List<UpdateAction<Cart>> updateActions = paymentsToRemove.stream().map(RemovePayment::of).collect(toList());
        updateActions.add(AddPayment.of(paymentToAdd));
        return updateActions;
    }

    private CompletionStage<Payment> createPayment(final Cart cart, final CheckoutPaymentFormData formData) {
        final PaymentDraft paymentDraft = PaymentDraftBuilder.of(cart.getTotalPrice())
                .paymentMethodInfo(paymentMethod(formData).orElse(null))
                .customer(Optional.ofNullable(cart.getCustomerId()).map(Customer::referenceOfId).orElse(null))
                .build();
        return sphereClient.execute(PaymentCreateCommand.of(paymentDraft));
    }

    private Optional<PaymentMethodInfo> paymentMethod(final CheckoutPaymentFormData formData) {
        return paymentSettings.paymentMethods().stream()
                .filter(paymentMethod -> formData.paymentMethod().equals(paymentMethod.getMethod()))
                .findAny();
    }

    private List<Reference<Payment>> findPaymentsToRemove(final Cart cart) {
        return Optional.ofNullable(cart.getPaymentInfo())
                .map(PaymentInfo::getPayments)
                .orElseGet(Collections::emptyList);
    }

    private void deletePreviousPayments(final List<Reference<Payment>> paymentsToRemove) {
        paymentsToRemove.forEach(payment -> {
            if (payment.getObj() != null) {
                sphereClient.execute(PaymentDeleteCommand.of(payment.getObj()));
            } else {
                LOGGER.warn("Could not delete Payment {} due to payment reference not expanded in cart", payment.getId());
            }
        });
    }
}
