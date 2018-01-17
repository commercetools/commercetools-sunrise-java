package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.AbstractUserResourceUpdater;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.ShoppingListUpdatedActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListUpdatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.ShoppingListUpdateCommandHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

final class MyWishlistUpdaterImpl extends AbstractUserResourceUpdater<ShoppingList, ShoppingListUpdateCommand> implements MyWishlistUpdater {

    @Inject
    MyWishlistUpdaterImpl(final SphereClient sphereClient, final HookRunner hookRunner, final MyWishlistInCache myWishlistInCache) {
        super(sphereClient, hookRunner, myWishlistInCache);
    }

    @Override
    protected ShoppingListUpdateCommand buildUpdateCommand(final List<? extends UpdateAction<ShoppingList>> updateActions, final ShoppingList resource) {
        return ShoppingListUpdateCommand.of(resource, updateActions);
    }

    @Override
    protected CompletionStage<ShoppingListUpdateCommand> runUpdateCommandHook(final HookRunner hookRunner, final ShoppingListUpdateCommand baseCommand) {
        return ShoppingListUpdateCommandHook.runHook(hookRunner, baseCommand);
    }

    @Override
    protected CompletionStage<ShoppingList> runActionHook(final HookRunner hookRunner, final ShoppingList resource,
                                                          final ExpansionPathContainer<ShoppingList> expansionPathContainer) {
        return ShoppingListUpdatedActionHook.runHook(hookRunner, resource, expansionPathContainer);
    }

    @Override
    protected void runUpdatedHook(final HookRunner hookRunner, final ShoppingList resource) {
        ShoppingListUpdatedHook.runHook(hookRunner, resource);
    }
}
