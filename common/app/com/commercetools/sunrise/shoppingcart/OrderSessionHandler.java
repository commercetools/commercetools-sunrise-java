package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.sessions.SessionHandlerBase;
import io.sphere.sdk.orders.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static java.util.Collections.singletonList;

public class OrderSessionHandler extends SessionHandlerBase<Order> {

    private static final Logger logger = LoggerFactory.getLogger(OrderSessionHandler.class);
    private static final String LAST_ORDER_ID_SESSION_KEY = "sunrise-last-order-id";

    public Optional<String> findLastOrderId(final Http.Session session) {
        return findValue(session, getLastOrderIdSessionKey());
    }

    @Override
    protected void overwriteRelatedValuesInSession(final Http.Session session, final Order order) {
        overwriteValue(session, getLastOrderIdSessionKey(), order.getId());
    }

    @Override
    protected Set<String> sessionKeys() {
        return new HashSet<>(singletonList(getLastOrderIdSessionKey()));
    }

    @Override
    protected Logger logger() {
        return logger;
    }

    protected String getLastOrderIdSessionKey() {
        return LAST_ORDER_ID_SESSION_KEY;
    }
}
