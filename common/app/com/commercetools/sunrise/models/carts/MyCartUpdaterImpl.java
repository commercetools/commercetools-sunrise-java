package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.AbstractUserResourceUpdater;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.CartUpdatedActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CartUpdatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CartUpdateCommandHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.expansion.ExpansionPathContainer;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

final class MyCartUpdaterImpl extends AbstractUserResourceUpdater<Cart, CartUpdateCommand> implements MyCartUpdater {

    @Inject
    MyCartUpdaterImpl(final SphereClient sphereClient, final HookRunner hookRunner, final MyCart myCart) {
        super(sphereClient, hookRunner, myCart);
    }

    @Override
    protected CartUpdateCommand buildUpdateCommand(final List<? extends UpdateAction<Cart>> updateActions, final Cart resource) {
        return CartUpdateCommand.of(resource, updateActions);
    }

    @Override
    protected CompletionStage<CartUpdateCommand> runRequestHook(final HookRunner hookRunner, final CartUpdateCommand baseCommand) {
        return CartUpdateCommandHook.runHook(hookRunner, baseCommand);
    }

    @Override
    protected CompletionStage<Cart> runResponseHook(final HookRunner hookRunner, final Cart resource,
                                                    final ExpansionPathContainer<Cart> expansionPathContainer) {
        return CartUpdatedActionHook.runHook(hookRunner, resource, expansionPathContainer);
    }

    @Override
    protected void runUpdatedHook(final HookRunner hookRunner, final Cart resource) {
        CartUpdatedHook.runHook(hookRunner, resource);
    }
}
