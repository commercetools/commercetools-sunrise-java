package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.actions.CartUpdatedActionHook;
import com.commercetools.sunrise.hooks.events.CartUpdatedHook;
import com.commercetools.sunrise.hooks.requests.CartUpdateCommandHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.client.SphereClient;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractCartUpdateExecutor extends AbstractSphereRequestExecutor {

    protected AbstractCartUpdateExecutor(final SphereClient sphereClient, final HookContext hookContext) {
        super(sphereClient, hookContext);
    }

    protected final CompletionStage<Cart> executeRequest(final Cart cart, final CartUpdateCommand baseCommand) {
        final CartUpdateCommand command = CartUpdateCommandHook.runHook(getHookContext(), baseCommand);
        if (!command.getUpdateActions().isEmpty()) {
            return getSphereClient().execute(command)
                    .thenComposeAsync(updatedCart -> CartUpdatedActionHook.runHook(getHookContext(), updatedCart, command), HttpExecution.defaultContext())
                    .thenApplyAsync(updatedCart -> {
                        CartUpdatedHook.runHook(getHookContext(), updatedCart);
                        return updatedCart;
                    }, HttpExecution.defaultContext());
        } else {
            return completedFuture(cart);
        }
    }
}
