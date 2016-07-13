package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.function.UnaryOperator;

public class OrderListFinderByCustomerId implements OrderListFinder<String> {

    @Inject
    private SphereClient sphereClient;

    @Override
    public CompletionStage<PagedQueryResult<Order>> findOrderList(final String customerId, final UnaryOperator<OrderQuery> filter) {
        final OrderQuery baseQuery = OrderQuery.of().byCustomerId(customerId);
        final OrderQuery query = filter.apply(baseQuery);
        return sphereClient.execute(query);
    }
}
