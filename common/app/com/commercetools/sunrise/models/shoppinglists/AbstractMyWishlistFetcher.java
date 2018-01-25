package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.AbstractHookRunner;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public abstract class AbstractMyWishlistFetcher extends AbstractHookRunner<Optional<ShoppingList>, ShoppingListQuery> implements MyWishlistFetcher {

    protected AbstractMyWishlistFetcher(final HookRunner hookRunner) {
        super(hookRunner);
    }

    @Override
    protected CompletionStage<Optional<ShoppingList>> runHook(final ShoppingListQuery request,
                                                              final Function<ShoppingListQuery, CompletionStage<Optional<ShoppingList>>> execution) {
        return hookRunner().run(MyWishlistFetcherHook.class, request, execution, h -> h::on);
    }
}
