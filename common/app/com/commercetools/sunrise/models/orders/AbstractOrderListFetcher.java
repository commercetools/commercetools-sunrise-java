package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.AbstractResourceFetcher;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.OrderPagedQueryResultLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.OrderQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractOrderListFetcher extends AbstractResourceFetcher<Order, OrderQuery, PagedQueryResult<Order>> implements MyOrderListFetcher {

    protected AbstractOrderListFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<PagedQueryResult<Order>> get() {
        return defaultRequest().map(this::executeRequest).orElseGet(() -> completedFuture(PagedQueryResult.empty()));
    }

    @Override
    protected CompletionStage<OrderQuery> runRequestHook(final HookRunner hookRunner, final OrderQuery baseRequest) {
        return OrderQueryHook.runHook(hookRunner, baseRequest);
    }

    @Override
    protected void runResourceLoadedHook(final HookRunner hookRunner, final PagedQueryResult<Order> result) {
        OrderPagedQueryResultLoadedHook.runHook(hookRunner, result);
    }
}
