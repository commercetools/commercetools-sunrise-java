package com.commercetools.sunrise.shoppingcart.checkout;

import com.commercetools.sunrise.common.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.hooks.HookContext;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.commands.OrderFromCartCreateCommand;

import java.util.concurrent.CompletionStage;

public abstract class AbstractOrderCreateExecutor extends AbstractSphereRequestExecutor {

    protected AbstractOrderCreateExecutor(final SphereClient sphereClient, final HookContext hookContext) {
        super(sphereClient, hookContext);
    }

    protected final CompletionStage<Order> executeRequest(final Cart cart, final OrderFromCartCreateCommand baseCommand) {
        return getSphereClient().execute(baseCommand);
    }

}
