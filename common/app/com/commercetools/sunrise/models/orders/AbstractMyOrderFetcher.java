package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.AbstractHookRunner;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractMyOrderFetcher extends AbstractHookRunner<Optional<Order>, OrderQuery> implements MyOrderFetcher {

    private final SphereClient sphereClient;

    protected AbstractMyOrderFetcher(final HookRunner hookRunner, final SphereClient sphereClient) {
        super(hookRunner);
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<Optional<Order>> get(final String orderIdentifier) {
        return buildRequest(orderIdentifier)
                .map(request -> runHook(request, r -> sphereClient.execute(r)
                        .thenApply(results -> selectResult(results, orderIdentifier))))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected CompletionStage<Optional<Order>> runHook(final OrderQuery request,
                                                       final Function<OrderQuery, CompletionStage<Optional<Order>>> execution) {
        return hookRunner().run(MyOrderFetcherHook.class, request, execution, h -> h::on);
    }

    protected abstract Optional<OrderQuery> buildRequest(String orderIdentifier);

    protected Optional<Order> selectResult(final PagedQueryResult<Order> results, final String orderIdentifier) {
        return results.head();
    }
}
