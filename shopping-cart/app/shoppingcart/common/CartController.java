package shoppingcart.common;

import com.neovisionaries.i18n.CountryCode;
import common.actions.NoCache;
import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.carts.queries.CartByIdGet;
import io.sphere.sdk.models.Address;
import myaccount.CustomerSessionUtils;
import play.libs.F;
import play.mvc.Http;
import shoppingcart.CartSessionKeys;
import shoppingcart.CartSessionUtils;

import java.util.Optional;

import static java.util.Arrays.asList;

@NoCache
public abstract class CartController extends SunriseController {

    public CartController(final ControllerDependency controllerDependency) {
        super(controllerDependency);
    }

    protected F.Promise<Cart> getOrCreateCart(final UserContext userContext, final Http.Session session) {
        return Optional.ofNullable(session(CartSessionKeys.CART_ID))
                .map(this::fetchCart)
                .orElseGet(() -> createCart(userContext))
                .flatMap(cart -> {
                    CartSessionUtils.overwriteCartSessionData(cart, session, userContext, reverseRouter());
                    final boolean hasDifferentCountry = !userContext.country().equals(cart.getCountry());
                    return hasDifferentCountry ? updateCartCountry(cart, userContext.country()) : F.Promise.pure(cart);
                });
    }

    private F.Promise<Cart> createCart(final UserContext userContext) {
        final Address address = Address.of(userContext.country());
        final CartDraft cartDraft = CartDraftBuilder.of(userContext.currency())
                .country(address.getCountry())
                .shippingAddress(address)
                .customerId(CustomerSessionUtils.getCustomerId(session()).orElse(null))
                .customerEmail(CustomerSessionUtils.getCustomerEmail(session()).orElse(null))
                .build();
        return sphere().execute(CartCreateCommand.of(cartDraft));
    }

    private F.Promise<Cart> fetchCart(final String cartId) {
        final CartByIdGet query = CartByIdGet.of(cartId).withExpansionPaths(m -> m.shippingInfo().shippingMethod());
        return sphere().execute(query);
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
                .orElseGet(() -> Address.of(country));
        final CartUpdateCommand updateCommand = CartUpdateCommand.of(cart,
                asList(SetShippingAddress.of(shippingAddress), SetCountry.of(country)));
        return sphere().execute(updateCommand);
    }

}
