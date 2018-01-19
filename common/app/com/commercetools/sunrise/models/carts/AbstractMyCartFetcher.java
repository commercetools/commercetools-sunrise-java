package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.AbstractSingleResourceFetcher;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.CartLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CartQueryHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractMyCartFetcher extends AbstractSingleResourceFetcher<Cart, CartQuery, PagedQueryResult<Cart>> implements MyCartFetcher {

    protected AbstractMyCartFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Optional<Cart>> get() {
        return defaultRequest().map(this::executeRequest).orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected final CompletionStage<CartQuery> runRequestHook(final HookRunner hookRunner, final CartQuery baseRequest) {
        return CartQueryHook.runHook(hookRunner, baseRequest);
    }

    @Override
    protected final void runResourceLoadedHook(final HookRunner hookRunner, final Cart resource) {
        hookRunner.run(CartLoadedHook.class, h -> h.onLoaded(resource));
    }
}
