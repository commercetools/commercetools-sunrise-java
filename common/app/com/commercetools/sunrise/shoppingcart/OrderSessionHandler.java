package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.sessions.SessionHandlerBase;
import io.sphere.sdk.orders.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import java.util.Optional;

public class OrderSessionHandler extends SessionHandlerBase<Order> {

    private static final Logger logger = LoggerFactory.getLogger(OrderSessionHandler.class);
    private static final String LAST_ORDER_ID_SESSION_KEY = "sunrise-last-order-id";

    public Optional<String> findLastOrderId(final Http.Session session) {
        return findValueByKey(session, getLastOrderIdSessionKey());
    }

    @Override
    protected void overwriteRelatedValuesInSession(final Http.Session session, final Order order) {
        overwriteStringValueByKey(session, getLastOrderIdSessionKey(), order.getId());
    }

    @Override
    protected void removeRelatedValuesFromSession(final Http.Session session) {
        removeValuesByKey(session, getLastOrderIdSessionKey());
    }

    @Override
    protected Logger logger() {
        return logger;
    }

    protected String getLastOrderIdSessionKey() {
        return LAST_ORDER_ID_SESSION_KEY;
    }
}
