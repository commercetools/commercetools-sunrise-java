package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.AbstractSingleResourceFetcher;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.ShoppingListQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * This base class provides an execution strategy to execute a {@link ShoppingListQuery} for a single {@link ShoppingList}
 * with the registred hooks {@link ShoppingListQueryHook} and {@link ShoppingListLoadedHook}.
 */
public abstract class AbstractMyWishlistFetcher extends AbstractSingleResourceFetcher<ShoppingList, ShoppingListQuery, PagedQueryResult<ShoppingList>> implements MyWishlistFetcher {

    protected AbstractMyWishlistFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Optional<ShoppingList>> get() {
        return defaultRequest().map(this::executeRequest).orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected final CompletionStage<ShoppingListQuery> runRequestHook(final HookRunner hookRunner, final ShoppingListQuery baseRequest) {
        return ShoppingListQueryHook.runHook(hookRunner, baseRequest);
    }

    @Override
    protected final void runResourceLoadedHook(final HookRunner hookRunner, final ShoppingList resource) {
        ShoppingListLoadedHook.runHook(hookRunner, resource);
    }
}
