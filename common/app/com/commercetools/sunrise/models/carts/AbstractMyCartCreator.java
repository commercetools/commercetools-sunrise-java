package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.AbstractResourceCreator;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.CartCreatedActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CartCreatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CartCreateCommandHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

public abstract class AbstractMyCartCreator extends AbstractResourceCreator<Cart, CartDraft, CartCreateCommand> implements MyCartCreator {

    private final MyCartInCache myCartInCache;

    protected AbstractMyCartCreator(final SphereClient sphereClient, final HookRunner hookRunner, final MyCartInCache myCartInCache) {
        super(sphereClient, hookRunner);
        this.myCartInCache = myCartInCache;
    }

    @Override
    protected CartCreateCommand buildRequest(final CartDraft draft) {
        return CartCreateCommand.of(draft);
    }

    @Override
    protected CompletionStage<Cart> executeRequest(final CartCreateCommand baseCommand) {
        final CompletionStage<Cart> resourceStage = super.executeRequest(baseCommand);
        resourceStage.thenAcceptAsync(myCartInCache::store, HttpExecution.defaultContext());
        return resourceStage;
    }

    @Override
    protected final CompletionStage<CartCreateCommand> runRequestHook(final HookRunner hookRunner, final CartCreateCommand baseCommand) {
        return CartCreateCommandHook.runHook(hookRunner, baseCommand);
    }

    @Override
    protected final void runCreatedHook(final HookRunner hookRunner, final Cart resource) {
        CartCreatedHook.runHook(hookRunner, resource);
    }

    @Override
    protected final CompletionStage<Cart> runActionHook(final HookRunner hookRunner, final Cart resource, final ExpansionPathContainer<Cart> expansionPathContainer) {
        return CartCreatedActionHook.runHook(hookRunner, resource, expansionPathContainer);
    }
}
