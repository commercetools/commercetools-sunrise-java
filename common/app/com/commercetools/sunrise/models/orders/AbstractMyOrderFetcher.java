package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.controllers.AbstractSingleQueryExecutor;
import com.commercetools.sunrise.core.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.OrderLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.OrderQueryHook;
import com.commercetools.sunrise.models.customers.SignUpFormData;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.commands.CustomerCreateCommand;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractMyOrderFetcher extends AbstractSingleQueryExecutor<Order, OrderQuery> implements MyOrderFetcher {

    protected AbstractMyOrderFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
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

    protected abstract Optional<OrderQuery> buildRequest(String identifier);
}
