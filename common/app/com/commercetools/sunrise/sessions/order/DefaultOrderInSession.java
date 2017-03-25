package com.commercetools.sunrise.sessions.order;

import com.commercetools.sunrise.sessions.DataFromResourceStoringOperations;
import com.commercetools.sunrise.sessions.SessionStrategy;
import io.sphere.sdk.orders.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Keeps the order ID in session.
 */
@Singleton
public class DefaultOrderInSession extends DataFromResourceStoringOperations<Order> implements OrderInSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderInSession.class);
    private static final String DEFAULT_LAST_ORDER_ID_SESSION_KEY = "sunrise-last-order-id";

    private final String lastOrderIdSessionKey;
    private final SessionStrategy session;

    @Inject
    public DefaultOrderInSession(final SessionStrategy session, final Configuration configuration) {
        this.lastOrderIdSessionKey = configuration.getString("session.order.lastOrderId", DEFAULT_LAST_ORDER_ID_SESSION_KEY);
        this.session = session;
    }

    @Override
    protected final Logger getLogger() {
        return LOGGER;
    }

    protected final SessionStrategy getSession() {
        return session;
    }

    @Override
    public Optional<String> findLastOrderId() {
        return session.findValueByKey(lastOrderIdSessionKey);
    }

    @Override
    public void store(@Nullable final Order order) {
        super.store(order);
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    protected void storeAssociatedData(final Order order) {
        session.overwriteValueByKey(lastOrderIdSessionKey, order.getId());
    }

    @Override
    protected void removeAssociatedData() {
        session.removeValueByKey(lastOrderIdSessionKey);
    }
}
