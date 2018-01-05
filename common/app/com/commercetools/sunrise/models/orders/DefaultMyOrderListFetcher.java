package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.CustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.queries.OrderQuery;

import javax.inject.Inject;
import java.util.Optional;

public class DefaultMyOrderListFetcher extends AbstractMyOrderListFetcher {

    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultMyOrderListFetcher(final SphereClient sphereClient, final HookRunner hookRunner,
                                        final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.customerInSession = customerInSession;
    }

    @Override
    protected Optional<OrderQuery> buildRequest() {
        return customerInSession.findId()
                .map(customerId -> OrderQuery.of()
                        .byCustomerId(customerId)
                        .withSort(order -> order.createdAt().sort().desc()));
    }
}
