package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.AbstractHookRunner;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.completedFuture;

public abstract class AbstractMyCustomerFetcher extends AbstractHookRunner<Optional<Customer>, CustomerQuery> implements MyCustomerFetcher {

    private final SphereClient sphereClient;

    protected AbstractMyCustomerFetcher(final HookRunner hookRunner, final SphereClient sphereClient) {
        super(hookRunner);
        this.sphereClient = sphereClient;
    }

    @Override
    public CompletionStage<Optional<Customer>> get() {
        return buildRequest()
                .map(request -> runHook(request, r -> sphereClient.execute(r).thenApply(this::selectResult)))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    @Override
    protected final CompletionStage<Optional<Customer>> runHook(final CustomerQuery request, final Function<CustomerQuery, CompletionStage<Optional<Customer>>> execution) {
        return hookRunner().run(MyCustomerFetcherHook.class, request, execution, h -> h::on);
    }

    protected abstract Optional<CustomerQuery> buildRequest();

    protected Optional<Customer> selectResult(final PagedQueryResult<Customer> results) {
        return results.head();
    }
}
