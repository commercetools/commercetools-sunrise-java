package shoppingcart.common;

import com.neovisionaries.i18n.CountryCode;
import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.carts.queries.CartByIdGet;
import io.sphere.sdk.models.Address;
import play.libs.F;
import play.mvc.Http;
import shoppingcart.CartSessionKeys;
import shoppingcart.CartSessionUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

public abstract class CartController extends SunriseController {
    public CartController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    protected F.Promise<Cart> getOrCreateCart(final UserContext userContext, final Http.Session session) {
        return Optional.ofNullable(session(CartSessionKeys.CART_ID))
                .map(cartId -> fetchCart(cartId, userContext))
                .orElse(createCart(userContext))
                .map(cart -> {
                    CartSessionUtils.overwriteCartSessionData(cart, session);
                    return cart;
                });
    }

    private F.Promise<Cart> createCart(final UserContext userContext) {
        final CartDraft cartDraft = CartDraft.of(userContext.currency()).withCountry(userContext.country());
        return sphere().execute(CartCreateCommand.of(cartDraft))
                .flatMap(cart -> updateCartCountry(cart, userContext.country()));
    }

    private F.Promise<Cart> fetchCart(final String cartId, final UserContext userContext) {
        final CartByIdGet query = CartByIdGet.of(cartId).withExpansionPaths(m -> m.shippingInfo().shippingMethod());
        return sphere().execute(query).flatMap(cart -> {
            final boolean hasDifferentCountry = !userContext.country().equals(cart.getCountry());
            return hasDifferentCountry ? updateCartCountry(cart, userContext.country()) : F.Promise.pure(cart);
        });
    }

    /**
     * Updates the country of the cart, both {@code country} and {@code shippingAddress} country fields.
     * This is necessary in order to obtain prices with tax calculation.
     * @param cart the cart which country needs to be updated
     * @param country the country to set in the cart
     * @return the promise of a cart with the given country
     */
    private F.Promise<Cart> updateCartCountry(final Cart cart, final CountryCode country) {
        // TODO Handle case where some line items do not exist for this country
        final Address shippingAddress = Optional.ofNullable(cart.getShippingAddress())
                .map(address -> address.withCountry(country))
                .orElse(Address.of(country));
        final CartUpdateCommand updateCommand = CartUpdateCommand.of(cart,
                asList(SetShippingAddress.of(shippingAddress), SetCountry.of(country)));
        return sphere().execute(updateCommand);
    }

}
