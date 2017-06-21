package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.ShoppingListCreatedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.ShoppingListCreateCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

/**
 * This base class provides an execution strategy for executing a {@link ShoppingListCreateCommand}
 * with the registered hooks {@link ShoppingListCreateCommandHook} and {@link ShoppingListCreatedHook}.
 */
public abstract class AbstractShoppingListCreateExecutor extends AbstractSphereRequestExecutor {

    protected AbstractShoppingListCreateExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<ShoppingList> executeRequest(final ShoppingListCreateCommand baseCommand) {
        final ShoppingListCreateCommand command = ShoppingListCreateCommandHook.runHook(getHookRunner(), baseCommand);
        return getSphereClient().execute(command)
                .thenApplyAsync(cart -> {
                    ShoppingListCreatedHook.runHook(getHookRunner(), cart);
                    return cart;
                }, HttpExecution.defaultContext());
    }
}
