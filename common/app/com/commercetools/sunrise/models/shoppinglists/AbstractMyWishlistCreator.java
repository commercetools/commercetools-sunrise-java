package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.AbstractHookRunner;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListCreateCommand;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class AbstractMyWishlistCreator extends AbstractHookRunner<ShoppingList, ShoppingListCreateCommand> implements MyWishlistCreator {

    private final SphereClient sphereClient;
    private final MyWishlist myWishlist;

    protected AbstractMyWishlistCreator(final HookRunner hookRunner, final SphereClient sphereClient,
                                        final MyWishlist myWishlist) {
        super(hookRunner);
        this.sphereClient = sphereClient;
        this.myWishlist = myWishlist;
    }

    @Override
    public CompletionStage<ShoppingList> get() {
        return runHook(buildRequest(), r -> {
            final CompletionStage<ShoppingList> resultStage = sphereClient.execute(r);
            resultStage.thenAcceptAsync(myWishlist::store, HttpExecution.defaultContext());
            return resultStage;
        });
    }

    @Override
    protected CompletionStage<ShoppingList> runHook(final ShoppingListCreateCommand request, final Function<ShoppingListCreateCommand, CompletionStage<ShoppingList>> execution) {
        return hookRunner().run(MyWishlistCreatorHook.class, request, execution, h -> h::on);
    }

    protected abstract ShoppingListCreateCommand buildRequest();
}
