package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.queries.CustomerQuery;
import io.sphere.sdk.customers.queries.CustomerQueryBuilder;

import javax.inject.Inject;
import java.util.Optional;

public class DefaultCustomerFetcher extends AbstractCustomerFetcher {

    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultCustomerFetcher(final SphereClient sphereClient, final HookRunner hookRunner,
                                     final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.customerInSession = customerInSession;
    }

    protected final CustomerInSession getCustomerInSession() {
        return customerInSession;
    }

    protected Optional<CustomerQuery> buildRequest() {
        return customerInSession.findId()
                .map(customerId -> CustomerQueryBuilder.of()
                        .plusPredicates(customer -> customer.id().is(customerId))
                        .build());
    }
}
