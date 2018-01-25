package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.carts.MyCart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.OrderFromCartDraft;
import io.sphere.sdk.orders.PaymentState;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;
import org.apache.commons.lang3.RandomStringUtils;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public final class DefaultOrderCreator extends AbstractOrderCreator implements OrderCreator {

    private final MyCart myCart;

    @Inject
    DefaultOrderCreator(final SphereClient sphereClient, final HookRunner hookRunner, final MyCart myCart) {
        super(hookRunner, sphereClient);
        this.myCart = myCart;
    }

    @Override
    protected CompletionStage<OrderFromCartCreateCommand> buildRequest() {
        return myCart.require()
                .thenApply(cart -> OrderFromCartDraft.of(cart, generateOrderNumber(), PaymentState.PENDING))
                .thenApply(OrderFromCartCreateCommand::of);
    }

    private String generateOrderNumber() {
        return RandomStringUtils.randomNumeric(8);
    }
}
