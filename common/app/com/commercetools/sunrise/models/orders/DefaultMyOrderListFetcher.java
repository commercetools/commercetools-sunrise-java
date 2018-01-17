package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.MyCustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.queries.OrderQuery;

import javax.inject.Inject;
import java.util.Optional;

final class DefaultMyOrderListFetcher extends AbstractOrderListFetcher {

    private final MyCustomerInSession myCustomerInSession;

    @Inject
    DefaultMyOrderListFetcher(final SphereClient sphereClient, final HookRunner hookRunner,
                                        final MyCustomerInSession myCustomerInSession) {
        super(sphereClient, hookRunner);
        this.myCustomerInSession = myCustomerInSession;
    }

    @Override
    public Optional<OrderQuery> defaultRequest() {
        return myCustomerInSession.findId()
                .map(customerId -> OrderQuery.of()
                        .byCustomerId(customerId)
                        .withSort(order -> order.createdAt().sort().desc()));
    }
}
