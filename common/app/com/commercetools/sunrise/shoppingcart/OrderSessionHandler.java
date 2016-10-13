package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.sessions.SessionHandlerBase;
import io.sphere.sdk.orders.Order;
import play.Configuration;
import play.mvc.Http;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class OrderSessionHandler extends SessionHandlerBase<Order> {

    private static final String DEFAULT_LAST_ORDER_ID_SESSION_KEY = "sunrise-last-order-id";
    private final String lastOrderIdSessionKey;

    @Inject
    public OrderSessionHandler(final Configuration configuration) {
        this.lastOrderIdSessionKey = configuration.getString("session.order.lastOrderId", DEFAULT_LAST_ORDER_ID_SESSION_KEY);
    }

    public Optional<String> findLastOrderId(final Http.Session session) {
        return findValueByKey(session, lastOrderIdSessionKey);
    }

    @Override
    protected void overwriteRelatedValuesInSession(final Http.Session session, final Order order) {
        overwriteStringValueByKey(session, lastOrderIdSessionKey, order.getId());
    }

    @Override
    protected void removeRelatedValuesFromSession(final Http.Session session) {
        removeValuesByKey(session, lastOrderIdSessionKey);
    }
}
