package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.AbstractHookRunner;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.client.SphereClient;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class AbstractMyCartCreator extends AbstractHookRunner<Cart, CartCreateCommand> implements MyCartCreator {

    private final SphereClient sphereClient;
    private final MyCart myCart;

    protected AbstractMyCartCreator(final HookRunner hookRunner, final SphereClient sphereClient, final MyCart myCart) {
        super(hookRunner);
        this.sphereClient = sphereClient;
        this.myCart = myCart;
    }

    @Override
    public CompletionStage<Cart> get() {
        return runHook(buildRequest(), r -> {
            final CompletionStage<Cart> resultStage = sphereClient.execute(r);
            resultStage.thenAcceptAsync(myCart::store, HttpExecution.defaultContext());
            return resultStage;
        });
    }

    @Override
    protected final CompletionStage<Cart> runHook(final CartCreateCommand request, final Function<CartCreateCommand, CompletionStage<Cart>> execution) {
        return hookRunner().run(MyCartCreatorHook.class, request, execution, h -> h::on);
    }

    protected abstract CartCreateCommand buildRequest();
}
