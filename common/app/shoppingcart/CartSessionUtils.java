package shoppingcart;

import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.MiniCart;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.json.SphereJsonUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.Http.Session;

import javax.annotation.Nullable;
import java.util.Optional;

public final class CartSessionUtils {
    private CartSessionUtils() {
    }

    public static MiniCart getMiniCart(final Session session) {
        return Optional.ofNullable(session.get(CartSessionKeys.MINI_CART))
                .map(miniCartAsJson -> SphereJsonUtils.readObject(miniCartAsJson, MiniCart.class))
                .orElseGet(MiniCart::new);
    }

    public static void overwriteCartSessionData(@Nullable final Cart cart, final Session session,
                                                final UserContext userContext, final ReverseRouter reverseRouter) {
        if (cart != null) {
            final String id = cart.getId();
            final MiniCart miniCart = new MiniCart(cart, userContext, reverseRouter);
            final String miniCartAsJson = Json.stringify(SphereJsonUtils.toJsonNode(miniCart));
            session.put(CartSessionKeys.CART_ID, id);
            session.put(CartSessionKeys.MINI_CART, miniCartAsJson);
            Logger.debug("Saved cart in session: ID \"{}\", Mini Cart: \"{}\"", miniCartAsJson);
        } else {
            removeCart(session);
        }
    }

    public static void removeCart(final Session session) {
        session.remove(CartSessionKeys.CART_ID);
        session.remove(CartSessionKeys.MINI_CART);
        Logger.debug("Removed cart from session");
    }
}