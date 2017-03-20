package com.commercetools.sunrise.myaccount;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.sessions.customer.CustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.customers.queries.CustomerQuery;
import io.sphere.sdk.customers.queries.CustomerQueryBuilder;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class CustomerFinderBySession extends AbstractSingleCustomerQueryExecutor implements CustomerFinder {

    private final CustomerInSession customerInSession;

    @Inject
    protected CustomerFinderBySession(final SphereClient sphereClient, final HookRunner hookRunner,
                                      final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.customerInSession = customerInSession;
    }

    @Override
    public CompletionStage<Optional<Customer>> get() {
        return customerInSession.findCustomerId()
                .map(customerId -> executeRequest(buildRequest(customerId)))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    protected CustomerQuery buildRequest(final String customerId) {
        return CustomerQueryBuilder.of()
                .plusPredicates(customer -> customer.id().is(customerId))
                .build();
    }
}
