package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.AbstractUserResourceUpdater;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.commands.UpdateAction;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class DefaultMyWishlistUpdater extends AbstractUserResourceUpdater<ShoppingList, ShoppingListUpdateCommand> implements MyWishlistUpdater {

    private final MyWishlistCreator myWishlistCreator;

    @Inject
    DefaultMyWishlistUpdater(final HookRunner hookRunner, final SphereClient sphereClient, final MyWishlist myWishlist,
                             final MyWishlistCreator myWishlistCreator) {
        super(hookRunner, sphereClient, myWishlist);
        this.myWishlistCreator = myWishlistCreator;
    }

    @Override
    public CompletionStage<ShoppingList> applyOrCreate(final List<? extends UpdateAction<ShoppingList>> updateActions) {
        return apply(updateActions)
                .thenComposeAsync(wishlistOpt -> wishlistOpt
                                .map(cart -> (CompletionStage<ShoppingList>) completedFuture(cart))
                                .orElseGet(() -> myWishlistCreator.get()
                                        .thenComposeAsync(createdWishlist -> executeRequest(buildRequest(createdWishlist, updateActions)),
                                                HttpExecution.defaultContext())),
                        HttpExecution.defaultContext());
    }

    @Override
    protected ShoppingListUpdateCommand buildRequest(final ShoppingList resource, final List<? extends UpdateAction<ShoppingList>> updateActions) {
        return ShoppingListUpdateCommand.of(resource, updateActions);
    }

    @Override
    protected CompletionStage<ShoppingList> runHook(final ShoppingListUpdateCommand request, final Function<ShoppingListUpdateCommand, CompletionStage<ShoppingList>> execution) {
        return hookRunner().run(MyWishlistUpdaterHook.class, request, execution, h -> h::on);
    }
}
