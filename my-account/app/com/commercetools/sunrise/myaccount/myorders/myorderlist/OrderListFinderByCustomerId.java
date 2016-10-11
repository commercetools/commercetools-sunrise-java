package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.hooks.requests.OrderQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class OrderListFinderByCustomerId implements OrderListFinder<String> {

    @Inject
    private SphereClient sphereClient;
    @Inject
    private RequestHookContext hookContext;

    @Override
    public CompletionStage<PagedQueryResult<Order>> findOrderList(final String customerId) {
        final OrderQuery baseQuery = OrderQuery.of()
                .byCustomerId(customerId)
                .withSort(order -> order.createdAt().sort().desc());
        final OrderQuery query = OrderQueryHook.runHook(hookContext, baseQuery);
        return sphereClient.execute(query);
    }
}
