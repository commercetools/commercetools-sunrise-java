package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.CartCreatedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.CartCreateCommandHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.client.SphereClient;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractCartCreateExecutor extends AbstractSphereRequestExecutor {

    protected AbstractCartCreateExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Cart> executeRequest(final CartCreateCommand baseCommand) {
        final CartCreateCommand command = CartCreateCommandHook.runHook(getHookRunner(), baseCommand);
        return getSphereClient().execute(command)
                .thenApplyAsync(cart -> {
                    CartCreatedHook.runHook(getHookRunner(), cart);
                    return cart;
                }, HttpExecution.defaultContext());
    }
}
