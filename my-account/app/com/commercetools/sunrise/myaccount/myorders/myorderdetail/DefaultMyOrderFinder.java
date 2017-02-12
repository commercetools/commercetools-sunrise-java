package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.hooks.events.OrderLoadedHook;
import com.commercetools.sunrise.myaccount.myorders.AbstractOrderQuerier;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class DefaultMyOrderFinder extends AbstractOrderQuerier implements MyOrderFinder {

    @Inject
    protected DefaultMyOrderFinder(final SphereClient sphereClient, final RequestHookContext hookContext) {
        super(sphereClient, hookContext);
    }

    @Override
    public CompletionStage<Optional<Order>> apply(final Customer customer, final String identifier) {
        return executeRequest(buildRequest(customer, identifier))
                .thenApply(PagedQueryResult::head)
                .thenApplyAsync(order -> {
                    order.ifPresent(this::runHookOnLoadedOrder);
                    return order;
                }, HttpExecution.defaultContext());
    }

    protected OrderQuery buildRequest(final Customer customer, final String identifier) {
        return OrderQuery.of().byCustomerId(customer.getId())
                .plusPredicates(order -> order.orderNumber().is(identifier))
                .plusExpansionPaths(order -> order.shippingInfo().shippingMethod())
                .plusExpansionPaths(order -> order.paymentInfo().payments())
                .withLimit(1);
    }

    private CompletionStage<?> runHookOnLoadedOrder(final Order order) {
        return OrderLoadedHook.runHook(getHookContext(), order);
    }
}
