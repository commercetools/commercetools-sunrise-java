package com.commercetools.sunrise.shoppingcart;

import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.json.SphereJsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.libs.Json;
import play.mvc.Http.Session;

import javax.annotation.Nullable;
import java.util.Optional;

public final class CartSessionUtils {
    private static final Logger logger = LoggerFactory.getLogger(CartSessionUtils.class);

    public static final String CART_ID_SESSION_KEY = "cart-id";
    public static final String MINI_CART_SESSION_KEY = "mini-cart";

    private CartSessionUtils() {
    }

    public static Optional<String> getCartId(final Session session) {
        return Optional.ofNullable(session.get(CART_ID_SESSION_KEY));
    }

    public static MiniCartBean getMiniCart(final Session session, final MiniCartBeanFactory miniCartBeanFactory) {
        return Optional.ofNullable(session.get(MINI_CART_SESSION_KEY))
                .map(miniCartAsJson -> SphereJsonUtils.readObject(miniCartAsJson, MiniCartBean.class))
                .orElseGet(() -> miniCartBeanFactory.create(null));
    }

    public static void overwriteCartSessionData(@Nullable final Cart cart, final Session session,
                                                final MiniCartBeanFactory miniCartBeanFactory) {
        if (cart != null) {
            final String id = cart.getId();
            final MiniCartBean miniCart = miniCartBeanFactory.create(cart);
            final String miniCartAsJson = Json.stringify(SphereJsonUtils.toJsonNode(miniCart));
            session.put(CART_ID_SESSION_KEY, id);
            session.put(MINI_CART_SESSION_KEY, miniCartAsJson);
            logger.debug("Saved cart in session: ID \"{}\", Mini Cart: \"{}\"", id, miniCartAsJson);
        } else {
            removeCartSessionData(session);
        }
    }

    public static void removeCartSessionData(final Session session) {
        session.remove(CART_ID_SESSION_KEY);
        session.remove(MINI_CART_SESSION_KEY);
        logger.debug("Removed cart from session");
    }
}