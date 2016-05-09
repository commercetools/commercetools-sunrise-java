package shoppingcart;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.json.SphereJsonUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.Http.Session;

import javax.annotation.Nullable;
import java.util.Optional;

public final class CartSessionUtils {

    public static final String CART_ID_SESSION_KEY = "cart-id";
    public static final String MINI_CART_SESSION_KEY = "mini-cart";

    private CartSessionUtils() {
    }

    public static Optional<String> getCartId(final Session session) {
        return Optional.ofNullable(session.get(CART_ID_SESSION_KEY));
    }

    public static MiniCartBean getMiniCart(final Session session) {
        return Optional.ofNullable(session.get(MINI_CART_SESSION_KEY))
                .map(miniCartAsJson -> SphereJsonUtils.readObject(miniCartAsJson, MiniCartBean.class))
                .orElseGet(MiniCartBean::new);
    }

    public static void overwriteCartSessionData(@Nullable final Cart cart, final Session session,
                                                final UserContext userContext, final ReverseRouter reverseRouter) {
        if (cart != null) {
            final String id = cart.getId();
            final MiniCartBean miniCart = new MiniCartBean(cart, userContext, reverseRouter);
            final String miniCartAsJson = Json.stringify(SphereJsonUtils.toJsonNode(miniCart));
            session.put(CART_ID_SESSION_KEY, id);
            session.put(MINI_CART_SESSION_KEY, miniCartAsJson);
            Logger.debug("Saved cart in session: ID \"{}\", Mini Cart: \"{}\"", id, miniCartAsJson);
        } else {
            removeCart(session);
        }
    }

    public static void removeCart(final Session session) {
        session.remove(CART_ID_SESSION_KEY);
        session.remove(MINI_CART_SESSION_KEY);
        Logger.debug("Removed cart from session");
    }
}