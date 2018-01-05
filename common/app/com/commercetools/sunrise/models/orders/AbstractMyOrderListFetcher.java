package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.controllers.AbstractQueryExecutor;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.OrderPagedQueryResultLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.OrderQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractMyOrderListFetcher extends AbstractQueryExecutor<Order, OrderQuery> implements MyOrderListFetcher {

    protected AbstractMyOrderListFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<PagedQueryResult<Order>> get() {
        return buildRequest().map(this::executeRequest).orElseGet(() -> completedFuture(PagedQueryResult.empty()));
    }

    @Override
    protected OrderQuery runQueryHook(final OrderQuery baseRequest) {
        return OrderQueryHook.runHook(getHookRunner(), baseRequest);
    }

    @Override
    protected CompletionStage<?> runPagedQueryResultLoadedHook(final PagedQueryResult<Order> result) {
        return OrderPagedQueryResultLoadedHook.runHook(getHookRunner(), result);
    }

    protected abstract Optional<OrderQuery> buildRequest();
}
