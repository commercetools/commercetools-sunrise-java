package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.controllers.AbstractSingleQueryExecutor;
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
public abstract class AbstractWishlistFetcher extends AbstractSingleQueryExecutor<ShoppingList, ShoppingListQuery, PagedQueryResult<ShoppingList>> implements WishlistFetcher {

    protected AbstractWishlistFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Optional<ShoppingList>> get() {
        return defaultRequest().map(this::executeRequest).orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected ShoppingListQuery runQueryHook(final ShoppingListQuery baseRequest) {
        return ShoppingListQueryHook.runHook(getHookRunner(), baseRequest);
    }

    @Override
    protected CompletionStage<?> runResourceLoadedHook(final ShoppingList resource) {
        return ShoppingListLoadedHook.runHook(getHookRunner(), resource);
    }
}
