package shoppingcart;

import com.fasterxml.jackson.databind.JsonNode;
import common.contexts.UserContext;
import common.controllers.ReverseRouter;
import common.models.MiniCart;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.json.SphereJsonUtils;
import play.Logger;
import play.libs.Json;
import play.mvc.Http.Session;

import java.util.Optional;

public final class CartSessionUtils {
    private CartSessionUtils() {
    }

    public static MiniCart getMiniCart(final Session session) {
        return Optional.ofNullable(session.get(CartSessionKeys.MINI_CART))
                .map(miniCartAsJson -> SphereJsonUtils.readObject(miniCartAsJson, MiniCart.class))
                .orElseGet(MiniCart::new);
    }

    public static void overwriteCartSessionData(final Cart cart, final Session session, final UserContext userContext,
                                                final ReverseRouter reverseRouter) {
        final JsonNode miniCartAsJson = SphereJsonUtils.toJsonNode(new MiniCart(cart, userContext, reverseRouter));
        session.put(CartSessionKeys.CART_ID, cart.getId());
        session.put(CartSessionKeys.MINI_CART, Json.stringify(miniCartAsJson));
        Logger.debug("Saved cart: " + session.toString());
    }

    public static void removeCart(final Session session) {
        session.remove(CartSessionKeys.CART_ID);
        session.remove(CartSessionKeys.MINI_CART);
    }
}