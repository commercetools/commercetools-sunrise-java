package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.controllers.SingleResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;
import io.sphere.sdk.orders.queries.OrderQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

// TODO Consider if show order can be done differently (cache, ctx.args...)
@ImplementedBy(DefaultOrderFetcher.class)
public interface OrderFetcher extends SingleResourceFetcher<Order, OrderQuery> {

    Optional<OrderQuery> defaultRequest(final String identifier);

    CompletionStage<Optional<Order>> get(final String identifier);

    default CompletionStage<Order> require(final String identifier) {
        return get(identifier).thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
