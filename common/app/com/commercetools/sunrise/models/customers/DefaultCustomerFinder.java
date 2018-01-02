package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerQuery;
import io.sphere.sdk.customers.queries.CustomerQueryBuilder;
import play.cache.CacheApi;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultCustomerFinder extends AbstractSingleCustomerQueryExecutor implements CustomerFinder {

    private final CustomerInSession customerInSession;
    private final CacheApi cacheApi;

    @Inject
    protected DefaultCustomerFinder(final SphereClient sphereClient, final HookRunner hookRunner,
                                    final CustomerInSession customerInSession, final CacheApi cacheApi) {
        super(sphereClient, hookRunner);
        this.customerInSession = customerInSession;
        this.cacheApi = cacheApi;
    }

    protected final CustomerInSession getCustomerInSession() {
        return customerInSession;
    }

    protected final CacheApi getCacheApi() {
        return cacheApi;
    }

    @Override
    public CompletionStage<Optional<Customer>> get() {
        return generateCacheKey()
                .map(this::findInCacheOrFetch)
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    protected CompletionStage<Optional<CustomerQuery>> buildRequest() {
        final Optional<CustomerQuery> query = customerInSession.findCustomerId()
                .map(customerId -> CustomerQueryBuilder.of()
                        .plusPredicates(customer -> customer.id().is(customerId))
                        .build());
        return completedFuture(query);
    }

    protected final Optional<String> generateCacheKey() {
        return customerInSession.findCustomerId().map(customerId -> "customer_" + customerId);
    }

    private CompletionStage<Optional<Customer>> findInCacheOrFetch(final String cacheKey) {
        final Customer nullableCustomer = cacheApi.get(cacheKey);
        return Optional.ofNullable(nullableCustomer)
                .map(customer -> (CompletionStage<Optional<Customer>>) completedFuture(Optional.of(customer)))
                .orElseGet(() -> fetchAndStoreCustomer(cacheKey));
    }

    private CompletionStage<Optional<Customer>> fetchAndStoreCustomer(final String cacheKey) {
        final CompletionStage<Optional<Customer>> customerStage = fetchCustomer();
        customerStage.thenAcceptAsync(customerOpt ->
                customerOpt.ifPresent(customer -> cacheApi.set(cacheKey, customer)),
                HttpExecution.defaultContext());
        return customerStage;
    }

    private CompletionStage<Optional<Customer>> fetchCustomer() {
        return buildRequest().thenComposeAsync(request ->
                        request.map(this::executeRequest).orElseGet(() -> completedFuture(Optional.empty())),
                HttpExecution.defaultContext());
    }
}
