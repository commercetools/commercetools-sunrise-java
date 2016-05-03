package shoppingcart;

import io.sphere.sdk.orders.Order;
import play.mvc.Http.Session;

import java.util.Optional;

public final class OrderSessionUtils {

    public static final String LAST_ORDER_ID_KEY = "lastOrderId";

    private OrderSessionUtils() {
    }

    public static Optional<String> getLastOrderId(final Session session) {
        return Optional.ofNullable(session.get(LAST_ORDER_ID_KEY));
    }

    public static void overwriteLastOrderId(final Order order, final Session session) {
        session.put(LAST_ORDER_ID_KEY, order.getId());
    }
}