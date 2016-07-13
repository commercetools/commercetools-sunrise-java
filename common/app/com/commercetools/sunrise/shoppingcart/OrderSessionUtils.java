package com.commercetools.sunrise.shoppingcart;

import io.sphere.sdk.orders.Order;
import play.mvc.Http.Session;

import java.util.Optional;

public final class OrderSessionUtils {

    public static final String LAST_ORDER_ID_SESSION_KEY = "lastOrderId";

    private OrderSessionUtils() {
    }

    public static Optional<String> getLastOrderId(final Session session) {
        return Optional.ofNullable(session.get(LAST_ORDER_ID_SESSION_KEY));
    }

    public static void overwriteLastOrderIdSessionData(final Order order, final Session session) {
        session.put(LAST_ORDER_ID_SESSION_KEY, order.getId());
    }
}