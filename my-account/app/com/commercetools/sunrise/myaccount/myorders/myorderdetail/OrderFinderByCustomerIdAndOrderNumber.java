package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.UnaryOperator;

public final class OrderFinderByCustomerIdAndOrderNumber implements OrderFinder<CustomerIdOrderNumberPair> {

    @Inject
    private SphereClient sphereClient;

    @Override
    public CompletionStage<Optional<Order>> findOrder(final CustomerIdOrderNumberPair customerIdOrderNumberPair, final UnaryOperator<OrderQuery> filter) {
        final OrderQuery baseOrderQuery = baseOrderQuery(customerIdOrderNumberPair.getCustomerId(), customerIdOrderNumberPair.getOrderNumber());
        final OrderQuery orderQuery = filter.apply(baseOrderQuery);
        return sphereClient.execute(orderQuery).thenApply(PagedQueryResult::head);
    }

    protected OrderQuery baseOrderQuery(final String customerId, final String orderNumber) {
        return OrderQuery.of().byCustomerId(customerId)
                .plusPredicates(order -> order.orderNumber().is(orderNumber))
                .plusExpansionPaths(order -> order.shippingInfo().shippingMethod())
                .plusExpansionPaths(order -> order.paymentInfo().payments())
                .withLimit(1);
    }
}
