package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.controllers.AbstractSingleQueryExecutor;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractCustomerFetcher extends AbstractSingleQueryExecutor<Customer, CustomerQuery> implements CustomerFetcher {

    protected AbstractCustomerFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Optional<Customer>> get() {
        return buildRequest().map(this::executeRequest).orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected CustomerQuery runQueryHook(final CustomerQuery baseRequest) {
        return CustomerQueryHook.runHook(getHookRunner(), baseRequest);
    }

    @Override
    protected CompletionStage<?> runResourceLoadedHook(final Customer resource) {
        return CustomerLoadedHook.runHook(getHookRunner(), resource);
    }

    protected abstract Optional<CustomerQuery> buildRequest();
}
