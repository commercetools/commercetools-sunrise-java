package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.carts.MyCartInCache;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.OrderFromCartDraft;
import io.sphere.sdk.orders.PaymentState;
import org.apache.commons.lang3.RandomStringUtils;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

final class DefaultOrderCreator extends AbstractOrderCreator implements OrderCreator {

    private final MyCartInCache myCartInCache;

    @Inject
    DefaultOrderCreator(final SphereClient sphereClient, final HookRunner hookRunner, final MyCartInCache myCartInCache) {
        super(sphereClient, hookRunner);
        this.myCartInCache = myCartInCache;
    }

    @Override
    public CompletionStage<OrderFromCartDraft> defaultDraft() {
        return myCartInCache.require().thenApply(cart -> OrderFromCartDraft.of(cart, generateOrderNumber(), generatePaymentState()));
    }

    private PaymentState generatePaymentState() {
        return PaymentState.PENDING;
    }

    private String generateOrderNumber() {
        return RandomStringUtils.randomNumeric(8);
    }
}
