package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.myaccount.myorders.AbstractSingleOrderQueryExecutor;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class DefaultMyOrderFinder extends AbstractSingleOrderQueryExecutor implements MyOrderFinder {

    @Inject
    protected DefaultMyOrderFinder(final SphereClient sphereClient, final RequestHookContext hookContext) {
        super(sphereClient, hookContext);
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
