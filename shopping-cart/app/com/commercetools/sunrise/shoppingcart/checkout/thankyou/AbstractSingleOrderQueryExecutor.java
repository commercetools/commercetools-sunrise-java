package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.events.OrderLoadedHook;
import com.commercetools.sunrise.hooks.requests.OrderQueryHook;
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
    protected AbstractSingleOrderQueryExecutor(final SphereClient sphereClient, final HookContext hookContext) {
        super(sphereClient, hookContext);
    }

    protected final CompletionStage<Optional<Order>> executeRequest(final OrderQuery baseQuery) {
        final OrderQuery request = OrderQueryHook.runHook(getHookContext(), baseQuery);
        return getSphereClient().execute(request)
                .thenApply(PagedQueryResult::head)
                .thenApplyAsync(orderOpt -> {
                    orderOpt.ifPresent(order -> OrderLoadedHook.runHook(getHookContext(), order));
                    return orderOpt;
                }, HttpExecution.defaultContext());
    }
}
