package com.commercetools.sunrise.models.customers;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.queries.CustomerQuery;
import io.sphere.sdk.customers.queries.CustomerQueryBuilder;

import javax.inject.Inject;
import java.util.Optional;

public final class DefaultMyCustomerFetcher extends AbstractMyCustomerFetcher {

    private final MyCustomerInSession myCustomerInSession;

    @Inject
    DefaultMyCustomerFetcher(final HookRunner hookRunner, final SphereClient sphereClient,
                             final MyCustomerInSession myCustomerInSession) {
        super(hookRunner, sphereClient);
        this.myCustomerInSession = myCustomerInSession;
    }

    @Override
    protected Optional<CustomerQuery> buildRequest() {
        return myCustomerInSession.findId()
                .map(customerId -> CustomerQueryBuilder.of()
                        .plusPredicates(customer -> customer.id().is(customerId))
                        .build());
    }
}
