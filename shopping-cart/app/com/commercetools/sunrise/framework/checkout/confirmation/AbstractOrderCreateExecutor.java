package com.commercetools.sunrise.framework.checkout.confirmation;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;

import java.util.concurrent.CompletionStage;

public abstract class AbstractOrderCreateExecutor extends AbstractSphereRequestExecutor {

    protected AbstractOrderCreateExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Order> executeRequest(final Cart cart, final OrderFromCartCreateCommand baseCommand) {
        return getSphereClient().execute(baseCommand);
    }

}
