package com.commercetools.sunrise.framework.checkout.thankyou;

import com.commercetools.sunrise.framework.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.framework.hooks.ctpevents.OrderLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctprequests.OrderQueryHook;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.queries.PagedQueryResult;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public abstract class AbstractSingleOrderQueryExecutor extends AbstractSphereRequestExecutor {

    @Inject
    protected AbstractSingleOrderQueryExecutor(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    protected final CompletionStage<Optional<Order>> executeRequest(final OrderQuery baseQuery) {
        final OrderQuery request = OrderQueryHook.runHook(getHookRunner(), baseQuery);
        return getSphereClient().execute(request)
                .thenApply(PagedQueryResult::head)
                .thenApplyAsync(orderOpt -> {
                    orderOpt.ifPresent(order -> OrderLoadedHook.runHook(getHookRunner(), order));
                    return orderOpt;
                }, HttpExecution.defaultContext());
    }
}
