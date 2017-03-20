package com.commercetools.sunrise.framework;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpactions.CartUpdatedActionHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.CartUpdatedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.CartUpdateCommandHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.client.SphereClient;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractCartUpdateExecutor extends AbstractSphereRequestExecutor {

    protected AbstractCartUpdateExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Cart> executeRequest(final Cart cart, final CartUpdateCommand baseCommand) {
        final CartUpdateCommand command = CartUpdateCommandHook.runHook(getHookRunner(), baseCommand);
        if (!command.getUpdateActions().isEmpty()) {
            return getSphereClient().execute(command)
                    .thenComposeAsync(updatedCart -> CartUpdatedActionHook.runHook(getHookRunner(), updatedCart, command), HttpExecution.defaultContext())
                    .thenApplyAsync(updatedCart -> {
                        CartUpdatedHook.runHook(getHookRunner(), updatedCart);
                        return updatedCart;
                    }, HttpExecution.defaultContext());
        } else {
            return completedFuture(cart);
        }
    }
}
