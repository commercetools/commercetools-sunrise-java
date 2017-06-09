package com.commercetools.sunrise.framework.checkout.payment;

import com.commercetools.sunrise.framework.AbstractCartUpdateExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.stream.Collectors.toList;

/**
 * Deletes previous payments associated with the cart and adds the new one.
 * The {@link PaymentInfo#getPayments()} references should be expanded using the expansion path {@code paymentInfo.payments} to properly work, otherwise previous payments will not be removed.
 */
public class DefaultCheckoutPaymentControllerAction extends AbstractCartUpdateExecutor implements CheckoutPaymentControllerAction {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutPaymentControllerAction.class);

    @Inject
    protected DefaultCheckoutPaymentControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Cart> apply(final PaymentMethodsWithCart paymentMethodsWithCart, final CheckoutPaymentFormData formData) {
        return createPayment(paymentMethodsWithCart, formData)
                .thenComposeAsync(payment -> {
                    final CartUpdateCommand command = buildRequest(paymentMethodsWithCart, formData, payment);
                    return executeRequest(paymentMethodsWithCart.getCart(), command)
                            .thenCompose(cart -> deletePreviousPayments(cart, payment));
                }, HttpExecution.defaultContext());
    }

    protected CartUpdateCommand buildRequest(final PaymentMethodsWithCart paymentMethodsWithCart, final CheckoutPaymentFormData formData, final Payment payment) {
        return CartUpdateCommand.of(paymentMethodsWithCart.getCart(), AddPayment.of(payment));
    }

    private CompletionStage<Cart> deletePreviousPayments(final Cart cart, final Payment payment) {
        final List<Payment> paymentsToRemove = findPaymentsToRemove(cart, payment);
        return removePaymentsFormCart(cart, paymentsToRemove).thenApply(updatedCart -> {
            paymentsToRemove.forEach(paymentToRemove -> getSphereClient().execute(PaymentDeleteCommand.of(paymentToRemove)));
            return updatedCart;
        });
    }

    private List<Payment> findPaymentsToRemove(final Cart cart, final Payment payment) {
        return Optional.ofNullable(cart.getPaymentInfo())
                .map(paymentInfo -> {
                    if (!hasReferencesExpanded(paymentInfo)) {
                        LOGGER.warn("Payments are not expanded in cart: the new payment information can be saved but the previous payments will not be removed.");
                    }
                    return paymentInfo.getPayments().stream()
                            .filter(paymentRef -> !paymentRef.referencesSameResource(payment))
                            .map(Reference::getObj)
                            .filter(Objects::nonNull)
                            .collect(toList());
                })
                .orElseGet(Collections::emptyList);
    }

    private CompletionStage<Cart> removePaymentsFormCart(final Cart cart, final List<Payment> paymentsToRemove) {
        final List<UpdateAction<Cart>> updateActions = paymentsToRemove.stream()
                .map(RemovePayment::of)
                .collect(toList());
        return getSphereClient().execute(CartUpdateCommand.of(cart, updateActions));
    }

    private CompletionStage<Payment> createPayment(final PaymentMethodsWithCart paymentMethodsWithCart, final CheckoutPaymentFormData formData) {
        final Cart cart = paymentMethodsWithCart.getCart();
        final PaymentMethodInfo paymentMethod = findPaymentMethod(paymentMethodsWithCart.getPaymentMethods(), formData);
        final PaymentDraft paymentDraft = PaymentDraftBuilder.of(cart.getTotalPrice())
                .paymentMethodInfo(paymentMethod)
                .customer(Optional.ofNullable(cart.getCustomerId()).map(Customer::referenceOfId).orElse(null))
                .build();
        return getSphereClient().execute(PaymentCreateCommand.of(paymentDraft));
    }

    private PaymentMethodInfo findPaymentMethod(final List<PaymentMethodInfo> paymentMethods, final CheckoutPaymentFormData formData) {
        return paymentMethods.stream()
                .filter(paymentMethod -> Objects.equals(paymentMethod.getMethod(), formData.paymentMethod()))
                .findAny()
                .orElseThrow(() -> new RuntimeException("No valid payment method found")); // Should not happen after validation
    }

    private boolean hasReferencesExpanded(final PaymentInfo paymentInfo) {
        return paymentInfo.getPayments().stream()
                .allMatch(reference -> reference.getObj() != null);
    }
}
