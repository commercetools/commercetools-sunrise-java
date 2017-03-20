package com.commercetools.sunrise.sessions.order;

import com.commercetools.sunrise.sessions.ResourceStoringOperations;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.orders.Order;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Keeps the order ID in session.
 */
@ImplementedBy(DefaultOrderInSession.class)
public interface OrderInSession extends ResourceStoringOperations<Order> {

    Optional<String> findLastOrderId();

    @Override
    void store(@Nullable final Order order);

    @Override
    void remove();
}
