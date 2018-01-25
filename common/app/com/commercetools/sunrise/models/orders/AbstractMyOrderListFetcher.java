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

public abstract class AbstractMyOrderListFetcher extends AbstractHookRunner<PagedQueryResult<Order>, OrderQuery> implements MyOrderListFetcher {

    private final SphereClient sphereClient;

    protected AbstractMyOrderListFetcher(final HookRunner hookRunner, final SphereClient sphereClient) {
        super(hookRunner);
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<PagedQueryResult<Order>> get() {
        return buildRequest()
                .map(request -> runHook(request, sphereClient::execute))
                .orElseGet(() -> completedFuture(PagedQueryResult.empty()));
    }

    @Override
    protected CompletionStage<PagedQueryResult<Order>> runHook(final OrderQuery request, final Function<OrderQuery, CompletionStage<PagedQueryResult<Order>>> execution) {
        return hookRunner().run(MyOrderListFetcherHook.class, request, execution, h -> h::on);
    }

    protected abstract Optional<OrderQuery> buildRequest();
}
