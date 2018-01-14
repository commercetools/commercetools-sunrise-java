package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.AbstractSingleResourceFetcher;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.OrderLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.OrderQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractOrderFetcher extends AbstractSingleResourceFetcher<Order, OrderQuery, PagedQueryResult<Order>> implements MyOrderFetcher {

    protected AbstractOrderFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Optional<Order>> get(final String identifier) {
        return defaultRequest(identifier).map(this::executeRequest).orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected final CompletionStage<OrderQuery> runRequestHook(final OrderQuery baseRequest) {
        return OrderQueryHook.runHook(getHookRunner(), baseRequest);
    }

    @Override
    protected final void runResourceLoadedHook(final Order resource) {
        OrderLoadedHook.runHook(getHookRunner(), resource);
    }
}
