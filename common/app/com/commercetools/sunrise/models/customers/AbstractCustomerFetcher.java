package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.AbstractSingleResourceFetcher;
import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.core.hooks.ctpevents.CustomerLoadedHook;
import com.commercetools.sunrise.core.hooks.ctprequests.CustomerQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractCustomerFetcher extends AbstractSingleResourceFetcher<Customer, CustomerQuery, PagedQueryResult<Customer>> implements MyCustomerFetcher {

    protected AbstractCustomerFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Optional<Customer>> get() {
        return defaultRequest().map(this::executeRequest).orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected final CompletionStage<CustomerQuery> runRequestHook(final CustomerQuery baseRequest) {
        return CustomerQueryHook.runHook(getHookRunner(), baseRequest);
    }

    @Override
    protected final void runResourceLoadedHook(final Customer resource) {
        CustomerLoadedHook.runHook(getHookRunner(), resource);
    }
}
