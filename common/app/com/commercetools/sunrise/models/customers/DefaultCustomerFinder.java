package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerQuery;
import io.sphere.sdk.customers.queries.CustomerQueryBuilder;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultCustomerFinder extends AbstractSingleCustomerQueryExecutor implements CustomerFinder {

    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultCustomerFinder(final SphereClient sphereClient, final HookRunner hookRunner,
                                    final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.customerInSession = customerInSession;
    }

    protected final CustomerInSession getCustomerInSession() {
        return customerInSession;
    }

    @Override
    public CompletionStage<Optional<Customer>> get() {
        return buildRequest().map(this::executeRequest).orElseGet(() -> completedFuture(Optional.empty()));
    }

    protected Optional<CustomerQuery> buildRequest() {
        return customerInSession.findId()
                .map(customerId -> CustomerQueryBuilder.of()
                        .plusPredicates(customer -> customer.id().is(customerId))
                        .build());
    }
}
