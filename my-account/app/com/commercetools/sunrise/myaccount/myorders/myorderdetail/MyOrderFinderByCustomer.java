package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class MyOrderFinderByCustomer extends AbstractSingleOrderQueryExecutor implements MyOrderFinder {

    @Inject
    protected MyOrderFinderByCustomer(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<Optional<Order>> apply(final Customer customer, final String identifier) {
        return executeRequest(buildRequest(customer, identifier));
    }

    protected OrderQuery buildRequest(final Customer customer, final String identifier) {
        return OrderQuery.of().byCustomerId(customer.getId())
                .plusPredicates(order -> order.orderNumber().is(identifier))
                .plusExpansionPaths(order -> order.shippingInfo().shippingMethod())
                .plusExpansionPaths(order -> order.paymentInfo().payments())
                .withLimit(1);
    }

}
