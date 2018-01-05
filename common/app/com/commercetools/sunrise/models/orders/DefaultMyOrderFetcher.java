package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.CustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.queries.OrderQuery;

import javax.inject.Inject;
import java.util.Optional;

public class DefaultMyOrderFetcher extends AbstractMyOrderFetcher {

    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultMyOrderFetcher(final SphereClient sphereClient, final HookRunner hookRunner, final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.customerInSession = customerInSession;
    }

    protected final CustomerInSession getCustomerInSession() {
        return customerInSession;
    }

    @Override
    protected Optional<OrderQuery> buildRequest(final String identifier) {
        return customerInSession.findId()
                .map(customerId -> OrderQuery.of().byCustomerId(customerId)
                        .plusPredicates(order -> order.orderNumber().is(identifier))
                        .plusExpansionPaths(order -> order.shippingInfo().shippingMethod())
                        .plusExpansionPaths(order -> order.paymentInfo().payments())
                        .plusExpansionPaths(order -> order.discountCodes().discountCode())
                        .withLimit(1));
    }

}
