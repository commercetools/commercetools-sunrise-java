package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListUpdatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.ShoppingListUpdateCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * This base class provides an execution strategy for executing a {@link ShoppingListUpdateCommand}
 * with the registered hooks {@link ShoppingListUpdateCommandHook} and {@link ShoppingListUpdatedHook}.
 */
public abstract class AbstractShoppingListUpdateExecutor extends AbstractSphereRequestExecutor {

    protected AbstractShoppingListUpdateExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<ShoppingList> executeRequest(final ShoppingList customer, final ShoppingListUpdateCommand baseCommand) {
        final ShoppingListUpdateCommand command = ShoppingListUpdateCommandHook.runHook(getHookRunner(), baseCommand);
        if (!command.getUpdateActions().isEmpty()) {
            return getSphereClient().execute(command)
                    .thenApplyAsync(updatedShoppingList -> {
                        ShoppingListUpdatedHook.runHook(getHookRunner(), updatedShoppingList);
                        return updatedShoppingList;
                    }, HttpExecution.defaultContext());
        } else {
            return completedFuture(customer);
        }
    }
}