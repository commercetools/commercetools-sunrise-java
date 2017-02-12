package com.commercetools.sunrise.shoppingcart.checkout.thankyou;

import com.commercetools.sunrise.common.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.hooks.HookContext;
import com.commercetools.sunrise.hooks.events.OrderLoadedHook;
import com.commercetools.sunrise.hooks.requests.OrderByIdGetHook;
import com.commercetools.sunrise.shoppingcart.OrderInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderByIdGet;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultOrderFinder extends AbstractSphereRequestExecutor implements OrderFinder {

    private final OrderInSession orderInSession;

    @Inject
    protected DefaultOrderFinder(final SphereClient sphereClient, final HookContext hookContext,
                                 final OrderInSession orderInSession) {
        super(sphereClient, hookContext);
        this.orderInSession = orderInSession;
    }

    @Override
    public CompletionStage<Optional<Order>> get() {
        return orderInSession.findLastOrderId()
                .map(orderId -> executeRequest(buildRequest(orderId)))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    protected OrderByIdGet buildRequest(final String orderId) {
        return OrderByIdGet.of(orderId).plusExpansionPaths(m -> m.paymentInfo().payments());
    }

    protected final CompletionStage<Optional<Order>> executeRequest(final OrderByIdGet baseRequest) {
        final OrderByIdGet request = OrderByIdGetHook.runHook(getHookContext(), baseRequest);
        return getSphereClient().execute(request)
                .thenApply(Optional::ofNullable)
                .thenApplyAsync(orderOpt -> {
                    orderOpt.ifPresent(order -> OrderLoadedHook.runHook(getHookContext(), order));
                    return orderOpt;
                }, HttpExecution.defaultContext());
    }
}
