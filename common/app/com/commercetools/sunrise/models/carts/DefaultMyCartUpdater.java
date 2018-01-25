package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.AbstractUserResourceUpdater;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class DefaultMyCartUpdater extends AbstractUserResourceUpdater<Cart, CartUpdateCommand> implements MyCartUpdater {

    private final MyCartCreator myCartCreator;

    @Inject
    DefaultMyCartUpdater(final HookRunner hookRunner, final SphereClient sphereClient, final MyCart myCart,
                         final MyCartCreator myCartCreator) {
        super(hookRunner, sphereClient, myCart);
        this.myCartCreator = myCartCreator;
    }

    @Override
    public CompletionStage<Cart> applyOrCreate(final List<? extends UpdateAction<Cart>> updateActions) {
        return apply(updateActions)
                .thenComposeAsync(cartOpt -> cartOpt
                        .map(cart -> (CompletionStage<Cart>) completedFuture(cart))
                        .orElseGet(() -> myCartCreator.get()
                                .thenComposeAsync(createdCart -> executeRequest(buildRequest(createdCart, updateActions)),
                                        HttpExecution.defaultContext())),
                        HttpExecution.defaultContext());
    }

    @Override
    protected CartUpdateCommand buildRequest(final Cart resource, final List<? extends UpdateAction<Cart>> updateActions) {
        return CartUpdateCommand.of(resource, updateActions);
    }

    @Override
    protected CompletionStage<Cart> runHook(final CartUpdateCommand request, final Function<CartUpdateCommand, CompletionStage<Cart>> execution) {
        return hookRunner().run(MyCartUpdaterHook.class, request, execution, h -> h::on);
    }
}
