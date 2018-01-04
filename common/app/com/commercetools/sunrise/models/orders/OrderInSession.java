package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.sessions.StoringOperations;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Keeps the order ID in session.
 */
@ImplementedBy(OrderInSessionImpl.class)
public interface OrderInSession extends StoringOperations<Order> {

    Optional<String> findLastOrderId();

    @Override
    void store(@Nullable final Order order);

    @Override
    void remove();
}
