package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.MyCustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.queries.OrderQuery;

import javax.inject.Inject;
import java.util.Optional;

public final class DefaultMyOrderListFetcher extends AbstractMyOrderListFetcher {

    private final MyCustomerInSession myCustomerInSession;

    @Inject
    DefaultMyOrderListFetcher(final HookRunner hookRunner, final SphereClient sphereClient,
                              final MyCustomerInSession myCustomerInSession) {
        super(hookRunner, sphereClient);
        this.myCustomerInSession = myCustomerInSession;
    }

    @Override
    protected Optional<OrderQuery> buildRequest() {
        return myCustomerInSession.findId()
                .map(customerId -> OrderQuery.of().byCustomerId(customerId)
                        .withSort(order -> order.createdAt().sort().desc()));
    }
}
