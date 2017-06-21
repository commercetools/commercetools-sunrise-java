package com.commercetools.sunrise.shoppingcart.checkout.confirmation;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.sessions.cart.CartInSession;
import com.commercetools.sunrise.sessions.order.OrderInSession;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.OrderFromCartDraft;
import io.sphere.sdk.orders.PaymentState;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;
import org.apache.commons.lang3.RandomStringUtils;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static play.libs.concurrent.HttpExecution.defaultContext;

public class DefaultCheckoutConfirmationControllerAction extends AbstractOrderCreateExecutor implements CheckoutConfirmationControllerAction {

    private final CartInSession cartInSession;
    private final OrderInSession orderInSession;

    @Inject
    protected DefaultCheckoutConfirmationControllerAction(final SphereClient sphereClient, final HookRunner hookRunner,
                                                          final CartInSession cartInSession, final OrderInSession orderInSession) {
        super(sphereClient, hookRunner);
        this.cartInSession = cartInSession;
        this.orderInSession = orderInSession;
    }

    @Override
    public CompletionStage<Order> apply(final Cart cart, final CheckoutConfirmationFormData formData) {
        return executeRequest(cart, buildRequest(cart, formData))
                .thenApplyAsync(order -> {
                    cartInSession.remove();
                    orderInSession.store(order);
                    return order;
                }, defaultContext());
    }

    protected OrderFromCartCreateCommand buildRequest(final Cart cart, final CheckoutConfirmationFormData formData) {
        final String orderNumber = getOrderNumber(cart, formData);
        final PaymentState paymentState = getPaymentState(cart, formData);
        final OrderFromCartDraft draft = OrderFromCartDraft.of(cart, orderNumber, paymentState);
        return OrderFromCartCreateCommand.of(draft);
    }

    protected String getOrderNumber(final Cart cart, final CheckoutConfirmationFormData formData) {
        return RandomStringUtils.randomNumeric(8);
    }

    protected PaymentState getPaymentState(final Cart cart, final CheckoutConfirmationFormData formData) {
        return PaymentState.PENDING;
    }
}
