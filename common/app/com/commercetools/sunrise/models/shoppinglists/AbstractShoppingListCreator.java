package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.AbstractResourceCreator;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.ShoppingListCreatedActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListCreatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.ShoppingListCreateCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.ShoppingListDraft;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;

import java.util.concurrent.CompletionStage;

public abstract class AbstractShoppingListCreator extends AbstractResourceCreator<ShoppingList, ShoppingListDraft, ShoppingListCreateCommand> implements MyWishlistCreator {

    protected AbstractShoppingListCreator(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    protected ShoppingListCreateCommand buildRequest(final ShoppingListDraft draft) {
        return ShoppingListCreateCommand.of(draft);
    }

    @Override
    protected final CompletionStage<ShoppingListCreateCommand> runCreateCommandHook(final HookRunner hookRunner, final ShoppingListCreateCommand baseCommand) {
        return ShoppingListCreateCommandHook.runHook(hookRunner, baseCommand);
    }

    @Override
    protected final void runCreatedHook(final HookRunner hookRunner, final ShoppingList resource) {
        ShoppingListCreatedHook.runHook(hookRunner, resource);
    }

    @Override
    protected final CompletionStage<ShoppingList> runActionHook(final HookRunner hookRunner, final ShoppingList resource, final ExpansionPathContainer<ShoppingList> expansionPathContainer) {
        return ShoppingListCreatedActionHook.runHook(hookRunner, resource, expansionPathContainer);
    }
}
