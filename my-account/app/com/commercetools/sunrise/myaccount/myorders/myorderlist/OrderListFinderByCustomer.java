package com.commercetools.sunrise.myaccount.myorders.myorderlist;

import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.hooks.requests.OrderQueryHook;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

final class OrderListFinderByCustomer implements OrderListFinder {

    private final CustomerFinder customerFinder;
    private final SphereClient sphereClient;
    private final RequestHookContext hookContext;

    @Inject
    OrderListFinderByCustomer(final CustomerFinder customerFinder, final SphereClient sphereClient, final RequestHookContext hookContext) {
        this.customerFinder = customerFinder;
        this.sphereClient = sphereClient;
        this.hookContext = hookContext;
    }

    @Override
    public CompletionStage<PagedQueryResult<Order>> findOrders() {
        return customerFinder.findCustomerId()
                .thenComposeAsync(customerId -> customerId
                            .map(this::fetchOrders)
                            .orElseGet(() -> completedFuture(PagedQueryResult.empty())),
                        HttpExecution.defaultContext());
    }

    private CompletionStage<PagedQueryResult<Order>> fetchOrders(final String customerId) {
        final OrderQuery baseQuery = OrderQuery.of()
                .byCustomerId(customerId)
                .withSort(order -> order.createdAt().sort().desc());
        final OrderQuery query = runHookOnOrderQuery(baseQuery);
        return sphereClient.execute(query);
    }

    private OrderQuery runHookOnOrderQuery(final OrderQuery baseQuery) {
        return OrderQueryHook.runHook(hookContext, baseQuery);
    }
}
