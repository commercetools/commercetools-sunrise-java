package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.AbstractResourceCreator;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctprequests.CartCreateCommandHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.client.SphereClient;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class AbstractMyCartCreator extends AbstractResourceCreator<Cart, CartDraft, CartCreateCommand> implements MyCartCreator {

    private final MyCart myCart;

    protected AbstractMyCartCreator(final SphereClient sphereClient, final HookRunner hookRunner, final MyCart myCart) {
        super(sphereClient, hookRunner);
        this.myCart = myCart;
    }

    @Override
    protected CartCreateCommand buildRequest(final CartDraft draft) {
        return CartCreateCommand.of(draft);
    }

    @Override
    protected CompletionStage<Cart> executeRequest(final CartCreateCommand baseCommand) {
        final CompletionStage<Cart> resourceStage = super.executeRequest(baseCommand);
        resourceStage.thenAcceptAsync(myCart::store, HttpExecution.defaultContext());
        return resourceStage;
    }

    @Override
    protected CompletionStage<Cart> runHook(final CartCreateCommand command, final Function<CartCreateCommand, CompletionStage<Cart>> execution) {
        return CartCreateCommandHook.run(getHookRunner(), command, execution);
    }
}
