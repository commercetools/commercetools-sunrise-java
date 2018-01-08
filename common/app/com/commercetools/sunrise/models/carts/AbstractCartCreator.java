package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.controllers.AbstractResourceCreator;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpactions.CartCreatedActionHook;
import com.commercetools.sunrise.core.hooks.ctpevents.CartCreatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CartCreateCommandHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.expansion.ExpansionPathContainer;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

public abstract class AbstractCartCreator extends AbstractResourceCreator<Cart, CartCreateCommand> implements CartCreator {

    protected AbstractCartCreator(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Cart> get(@Nullable final CartDraft template) {
        return executeRequest(buildRequest(template));
    }

    protected abstract CartCreateCommand buildRequest(@Nullable CartDraft template);

    @Override
    protected CartCreateCommand runCreateCommandHook(final HookRunner hookRunner, final CartCreateCommand baseCommand) {
        return CartCreateCommandHook.runHook(hookRunner, baseCommand);
    }

    @Override
    protected CompletionStage<?> runCreatedHook(final HookRunner hookRunner, final Cart resource) {
        return CartCreatedHook.runHook(hookRunner, resource);
    }

    @Override
    protected CompletionStage<Cart> runActionHook(final HookRunner hookRunner, final Cart resource, final ExpansionPathContainer<Cart> expansionPathContainer) {
        return CartCreatedActionHook.runHook(hookRunner, resource, expansionPathContainer);
    }
}
