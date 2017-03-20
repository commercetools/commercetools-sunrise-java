package com.commercetools.sunrise.framework.checkout.thankyou;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.sessions.order.OrderInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.orders.queries.OrderQueryBuilder;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class OrderCreatedFinderBySession extends AbstractSingleOrderQueryExecutor implements OrderCreatedFinder {

    private final OrderInSession orderInSession;

    @Inject
    protected OrderCreatedFinderBySession(final SphereClient sphereClient, final HookRunner hookRunner,
                                          final OrderInSession orderInSession) {
        super(sphereClient, hookRunner);
        this.orderInSession = orderInSession;
    }

    @Override
    public CompletionStage<Optional<Order>> get() {
        return orderInSession.findLastOrderId()
                .map(orderId -> executeRequest(buildRequest(orderId)))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    protected OrderQuery buildRequest(final String orderId) {
        return OrderQueryBuilder.of()
                .plusPredicates(order -> order.id().is(orderId))
                .plusExpansionPaths(m -> m.paymentInfo().payments())
                .build();
    }
}
