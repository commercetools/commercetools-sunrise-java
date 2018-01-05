package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.controllers.AbstractResourceCreator;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.CartCreatedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CartCreateCommandHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.client.SphereClient;

import java.util.concurrent.CompletionStage;

public abstract class AbstractCartCreator extends AbstractResourceCreator<Cart, CartCreateCommand> implements CartCreator {

    protected AbstractCartCreator(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Cart> get() {
        return executeRequest(buildRequest());
    }

    protected abstract CartCreateCommand buildRequest();

    @Override
    protected CartCreateCommand runCreateCommandHook(final CartCreateCommand baseCommand) {
        return CartCreateCommandHook.runHook(getHookRunner(), baseCommand);
    }

    @Override
    protected CompletionStage<?> runCreatedHook(final Cart resource) {
        return CartCreatedHook.runHook(getHookRunner(), resource);
    }
}
