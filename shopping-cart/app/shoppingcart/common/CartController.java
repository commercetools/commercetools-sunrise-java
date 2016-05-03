package shoppingcart.common;

import com.google.inject.Inject;
import com.neovisionaries.i18n.CountryCode;
import common.actions.NoCache;
import common.contexts.UserContext;
import common.controllers.ControllerDependency;
import common.controllers.SunriseController;
import common.models.ProductDataConfig;
import io.sphere.sdk.carts.*;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.carts.queries.CartByCustomerIdGet;
import io.sphere.sdk.carts.queries.CartByIdGet;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodsByCartGet;
import myaccount.CustomerSessionUtils;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;
import shoppingcart.CartLikeBean;
import shoppingcart.CartSessionUtils;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@NoCache
public abstract class CartController extends SunriseController {

    protected final ProductDataConfig productDataConfig;

    @Inject
    public CartController(final ControllerDependency controllerDependency, final ProductDataConfig productDataConfig) {
        super(controllerDependency);
        this.productDataConfig = productDataConfig;
    }

    protected CartLikeBean createCartLikeBean(final CartLike<?> cartLike, final UserContext userContext) {
        return new CartLikeBean(cartLike, userContext, productDataConfig, reverseRouter());
    }

    protected CompletionStage<Cart> getOrCreateCart(final UserContext userContext, final Http.Session session) {
        return fetchCart(userContext, session)
                .thenComposeAsync(cart -> {
                    CartSessionUtils.overwriteCartSessionData(cart, session, userContext, reverseRouter());
                    final boolean hasDifferentCountry = !userContext.country().equals(cart.getCountry());
                    return hasDifferentCountry ? updateCartCountry(cart, userContext.country()) : completedFuture(cart);
                }, HttpExecution.defaultContext());
    }

    protected CompletionStage<List<ShippingMethod>> getShippingMethods(final Http.Session session) {
        return CartSessionUtils.getCartId(session)
                .map(cartId -> sphere().execute(ShippingMethodsByCartGet.of(cartId)))
                .orElseGet(() -> completedFuture(emptyList()));
    }

    protected CompletionStage<Cart> fetchCart(final UserContext userContext, final Http.Session session) {
        return CustomerSessionUtils.getCustomerId(session)
                .map(customerId -> fetchCartByCustomerOrNew(customerId, userContext))
                .orElseGet(() -> CartSessionUtils.getCartId(session)
                        .map(cartId -> fetchCartByIdOrNew(cartId, userContext))
                        .orElseGet(() -> createCart(userContext)));
    }

    protected CompletionStage<Cart> createCart(final UserContext userContext) {
        final Address address = Address.of(userContext.country());
        final CartDraft cartDraft = CartDraftBuilder.of(userContext.currency())
                .country(address.getCountry())
                .shippingAddress(address)
                .customerId(CustomerSessionUtils.getCustomerId(session()).orElse(null))
                .customerEmail(CustomerSessionUtils.getCustomerEmail(session()).orElse(null))
                .build();
        return sphere().execute(CartCreateCommand.of(cartDraft));
    }

    protected CompletionStage<Cart> fetchCartByIdOrNew(final String cartId, final UserContext userContext) {
        final CartByIdGet query = CartByIdGet.of(cartId)
                .plusExpansionPaths(c -> c.shippingInfo().shippingMethod()) // TODO pass as an optional parameter to avoid expanding always
                .plusExpansionPaths(c -> c.paymentInfo().payments());
        return sphere().execute(query)
                .thenComposeAsync(cart -> validCartOrNew(cart, userContext), HttpExecution.defaultContext());
    }

    protected CompletionStage<Cart> fetchCartByCustomerOrNew(final String customerId, final UserContext userContext) {
        final CartByCustomerIdGet query = CartByCustomerIdGet.of(customerId)
                .plusExpansionPaths(c -> c.shippingInfo().shippingMethod()) // TODO pass as an optional parameter to avoid expanding always
                .plusExpansionPaths(c -> c.paymentInfo().payments());
        return sphere().execute(query)
                .thenComposeAsync(cart -> validCartOrNew(cart, userContext), HttpExecution.defaultContext());
    }

    protected CompletionStage<Cart> validCartOrNew(@Nullable final Cart cart, final UserContext userContext) {
        return Optional.ofNullable(cart)
                .filter(c -> c.getCartState().equals(CartState.ACTIVE))
                .map((value) -> (CompletionStage<Cart>) completedFuture(value))
                .orElseGet(() -> createCart(userContext));
    }

    /**
     * Updates the country of the cart, both {@code country} and {@code shippingAddress} country fields.
     * This is necessary in order to obtain prices with tax calculation.
     * @param cart the cart which country needs to be updated
     * @param country the country to set in the cart
     * @return the completionStage of a cart with the given country
     */
    protected CompletionStage<Cart> updateCartCountry(final Cart cart, final CountryCode country) {
        // TODO Handle case where some line items do not exist for this country
        final Address shippingAddress = Optional.ofNullable(cart.getShippingAddress())
                .map(address -> address.withCountry(country))
                .orElseGet(() -> Address.of(country));
        final CartUpdateCommand updateCommand = CartUpdateCommand.of(cart,
                asList(SetShippingAddress.of(shippingAddress), SetCountry.of(country)));
        return sphere().execute(updateCommand);
    }

}
