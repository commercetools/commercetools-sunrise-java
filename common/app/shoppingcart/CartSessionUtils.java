package shoppingcart;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.LineItem;
import play.Logger;
import play.mvc.Http.Session;

import java.util.Optional;

public final class CartSessionUtils {
    private CartSessionUtils() {
    }

    public static long getCartItemCount(final Session session) {
        return Optional.ofNullable(session.get(CartSessionKeys.CART_ITEM_COUNT))
                .map(Long::parseLong)
                .orElse(0L);
    }

    public static void overwriteCartSessionData(final Cart cart, final Session session) {
        final long itemCount = cart.getLineItems().stream().mapToLong(LineItem::getQuantity).sum();
        session.put(CartSessionKeys.CART_ID, cart.getId());
        session.put(CartSessionKeys.CART_ITEM_COUNT, String.valueOf(itemCount));
        Logger.debug(session.toString());
    }

    public static void removeCart(final Session session) {
        session.remove(CartSessionKeys.CART_ID);
        session.remove(CartSessionKeys.CART_ITEM_COUNT);
    }
}