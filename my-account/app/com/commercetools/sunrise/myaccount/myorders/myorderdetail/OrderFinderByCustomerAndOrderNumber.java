package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.hooks.events.OrderLoadedHook;
import com.commercetools.sunrise.hooks.requests.OrderQueryHook;
import com.commercetools.sunrise.myaccount.CustomerFinder;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

final class OrderFinderByCustomerAndOrderNumber implements OrderFinder {

    private final CustomerFinder customerFinder;
    private final SphereClient sphereClient;
    private final RequestHookContext hookContext;

    @Inject
    OrderFinderByCustomerAndOrderNumber(final CustomerFinder customerFinder, final SphereClient sphereClient, final RequestHookContext hookContext) {
        this.customerFinder = customerFinder;
        this.sphereClient = sphereClient;
        this.hookContext = hookContext;
    }

    @Override
    public CompletionStage<Optional<Order>> findOrder(final String orderNumber) {
        return customerFinder.findCustomerId()
                .thenComposeAsync(customerIdOpt -> customerIdOpt
                        .map(customerId -> findOrder(customerId, orderNumber))
                        .orElseGet(() -> completedFuture(Optional.empty())));
    }

    private CompletionStage<Optional<Order>> findOrder(final String customerId, final String orderNumber) {
        final OrderQuery baseOrderQuery = baseOrderQuery(customerId, orderNumber);
        final OrderQuery orderQuery = runHookOnOrderQuery(baseOrderQuery);
        final CompletionStage<Optional<Order>> orderStage = sphereClient.execute(orderQuery)
                .thenApply(PagedQueryResult::head);
        orderStage.thenAcceptAsync(order -> order.ifPresent(this::runHookOnLoadedOrder), HttpExecution.defaultContext());
        return orderStage;
    }

    private OrderQuery baseOrderQuery(final String customerId, final String orderNumber) {
        return OrderQuery.of().byCustomerId(customerId)
                .plusPredicates(order -> order.orderNumber().is(orderNumber))
                .plusExpansionPaths(order -> order.shippingInfo().shippingMethod())
                .plusExpansionPaths(order -> order.paymentInfo().payments())
                .withLimit(1);
    }

    private OrderQuery runHookOnOrderQuery(final OrderQuery baseOrderQuery) {
        return OrderQueryHook.runHook(hookContext, baseOrderQuery);
    }

    private CompletionStage<?> runHookOnLoadedOrder(final Order order) {
        return OrderLoadedHook.runHook(hookContext, order);
    }
}
