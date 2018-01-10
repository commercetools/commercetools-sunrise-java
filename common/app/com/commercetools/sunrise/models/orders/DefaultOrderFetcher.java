package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.orders.queries.OrderQuery;
import io.sphere.sdk.orders.queries.OrderQueryBuilder;

import javax.inject.Inject;
import java.util.Optional;

public class DefaultOrderFetcher extends AbstractOrderFetcher {

    @Inject
    protected DefaultOrderFetcher(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public Optional<OrderQuery> defaultRequest(final String identifier) {
        return Optional.of(OrderQueryBuilder.of()
                .plusPredicates(order -> order.id().is(identifier))
                .plusExpansionPaths(m -> m.paymentInfo().payments())
                .plusExpansionPaths(m -> m.discountCodes().discountCode())
                .plusExpansionPaths(m -> m.shippingInfo().shippingMethod())
                .build());
    }
}
