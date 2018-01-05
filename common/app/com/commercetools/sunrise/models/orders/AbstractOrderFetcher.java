package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.controllers.AbstractSingleQueryExecutor;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.OrderLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.OrderQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractOrderFetcher extends AbstractSingleQueryExecutor<Order, OrderQuery> implements OrderFetcher {

    protected AbstractOrderFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Optional<Order>> apply(final String identifier) {
        return buildRequest(identifier).map(this::executeRequest).orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected OrderQuery runQueryHook(final OrderQuery baseRequest) {
        return OrderQueryHook.runHook(getHookRunner(), baseRequest);
    }

    @Override
    protected CompletionStage<?> runResourceLoadedHook(final Order resource) {
        return OrderLoadedHook.runHook(getHookRunner(), resource);
    }

    protected abstract Optional<OrderQuery> buildRequest(final String identifier);
}
