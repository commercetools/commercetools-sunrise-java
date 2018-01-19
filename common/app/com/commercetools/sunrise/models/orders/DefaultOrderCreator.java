package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.carts.MyCart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.OrderFromCartDraft;
import io.sphere.sdk.orders.PaymentState;
import org.apache.commons.lang3.RandomStringUtils;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultOrderCreator extends AbstractOrderCreator implements OrderCreator {

    private final MyCart myCart;

    @Inject
    DefaultOrderCreator(final SphereClient sphereClient, final HookRunner hookRunner, final MyCart myCart) {
        super(sphereClient, hookRunner);
        this.myCart = myCart;
    }

    @Override
    public CompletionStage<OrderFromCartDraft> defaultDraft() {
        return myCart.require().thenApply(cart -> OrderFromCartDraft.of(cart, generateOrderNumber(), generatePaymentState()));
    }

    private PaymentState generatePaymentState() {
        return PaymentState.PENDING;
    }

    private String generateOrderNumber() {
        return RandomStringUtils.randomNumeric(8);
    }
}
