package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.controllers.AbstractResourceCreator;
import com.commercetools.sunrise.core.controllers.ResourceCreator;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListCreatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.ShoppingListCreateCommandHook;
import com.commercetools.sunrise.models.customers.SignUpFormData;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;

import java.util.concurrent.CompletionStage;

/**
 * This base class provides an execution strategy for executing a {@link ShoppingListCreateCommand}
 * with the registered hooks {@link ShoppingListCreateCommandHook} and {@link ShoppingListCreatedHook}.
 */
public abstract class AbstractShoppingListCreateExecutor extends AbstractResourceCreator<ShoppingList, ShoppingListCreateCommand> implements WishlistCreator {

    protected AbstractShoppingListCreateExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<ShoppingList> get() {
        return executeRequest(buildRequest());
    }

    @Override
    protected final ShoppingListCreateCommand runCreateCommandHook(final ShoppingListCreateCommand baseCommand) {
        return ShoppingListCreateCommandHook.runHook(getHookRunner(), baseCommand);
    }

    @Override
    protected final CompletionStage<?> runCreatedHook(final ShoppingList resource) {
        return ShoppingListCreatedHook.runHook(getHookRunner(), resource);
    }

    protected abstract ShoppingListCreateCommand buildRequest();
}
