package com.commercetools.sunrise.myaccount.myorders.myorderdetail;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.OrderLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.OrderQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public abstract class AbstractSingleOrderQueryExecutor extends AbstractSphereRequestExecutor {

    protected AbstractSingleOrderQueryExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Optional<Order>> executeRequest(final OrderQuery baseQuery) {
        final OrderQuery query = OrderQueryHook.runHook(getHookRunner(), baseQuery);
        return getSphereClient().execute(query)
                .thenApply(PagedQueryResult::head)
                .thenApplyAsync(orderOpt -> {
                    orderOpt.ifPresent(order -> OrderLoadedHook.runHook(getHookRunner(), order));
                    return orderOpt;
                }, HttpExecution.defaultContext());
    }
}
