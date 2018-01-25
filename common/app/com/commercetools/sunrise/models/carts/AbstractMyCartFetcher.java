package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.AbstractHookRunner;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractMyCartFetcher extends AbstractHookRunner<Optional<Cart>, CartQuery> implements MyCartFetcher {

    private final SphereClient sphereClient;

    protected AbstractMyCartFetcher(final HookRunner hookRunner, final SphereClient sphereClient) {
        super(hookRunner);
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<Optional<Cart>> get() {
        return buildRequest()
                .map(request -> runHook(request, r -> sphereClient.execute(r).thenApply(this::selectResult)))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected final CompletionStage<Optional<Cart>> runHook(final CartQuery request,
                                                            final Function<CartQuery, CompletionStage<Optional<Cart>>> execution) {
        return hookRunner().run(MyCartFetcherHook.class, request, execution, h -> h::on);
    }

    protected abstract Optional<CartQuery> buildRequest();

    protected Optional<Cart> selectResult(final PagedQueryResult<Cart> results) {
        return results.head();
    }
}
