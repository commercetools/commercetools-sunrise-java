package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyOrderFetcher.class)
public interface MyOrderFetcher extends ResourceFetcher {

    CompletionStage<Optional<Order>> get(final String identifier);

    default CompletionStage<Order> require(final String identifier) {
        return get(identifier).thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
