package com.commercetools.sunrise.models.orders;

import com.commercetools.sunrise.core.sessions.StoringStrategy;
import io.sphere.sdk.orders.Order;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Keeps the order ID in session.
 */
@Singleton
final class OrderInSessionImpl implements OrderInSession {

    private final String sessionKey;
    private final StoringStrategy storingStrategy;

    @Inject
    OrderInSessionImpl(final Configuration configuration, final StoringStrategy storingStrategy) {
        this.sessionKey = configuration.getString("sunrise.orders.sessionKey");
        this.storingStrategy = storingStrategy;
    }

    @Override
    public Optional<String> findLastOrderId() {
        return storingStrategy.findInSession(sessionKey);
    }

    @Override
    public void store(@Nullable final Order order) {
        final String orderId = order != null ? order.getId() : null;
        storingStrategy.overwriteInSession(sessionKey, orderId);
    }

    @Override
    public void remove() {
        storingStrategy.removeFromSession(sessionKey);
    }
}
