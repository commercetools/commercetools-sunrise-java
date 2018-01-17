package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.MyCustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.queries.OrderQuery;

import javax.inject.Inject;
import java.util.Optional;

final class DefaultMyOrderFetcher extends AbstractOrderFetcher {

    private final MyCustomerInSession myCustomerInSession;

    @Inject
    DefaultMyOrderFetcher(final SphereClient sphereClient, final HookRunner hookRunner, final MyCustomerInSession myCustomerInSession) {
        super(sphereClient, hookRunner);
        this.myCustomerInSession = myCustomerInSession;
    }

    @Override
    public Optional<OrderQuery> defaultRequest(final String identifier) {
        return myCustomerInSession.findId()
                .map(customerId -> OrderQuery.of().byCustomerId(customerId)
                        .plusPredicates(order -> order.orderNumber().is(identifier))
                        .plusExpansionPaths(order -> order.shippingInfo().shippingMethod())
                        .plusExpansionPaths(order -> order.paymentInfo().payments())
                        .plusExpansionPaths(order -> order.discountCodes().discountCode())
                        .withLimit(1));
    }

}
