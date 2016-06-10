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
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.carts.queries.CartQueryBuilder;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodsByCartGet;
import com.commercetools.sunrise.myaccount.CustomerSessionUtils;
import play.libs.concurrent.HttpExecution;
import play.mvc.Http;
import shoppingcart.CartLikeBean;
import shoppingcart.CartSessionUtils;

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
        final CompletionStage<Cart> cartFuture = fetchCart(userContext, session)
                .thenComposeAsync(cart -> updateCartWithUserPreferences(cart, userContext), HttpExecution.defaultContext());
        cartFuture.thenAcceptAsync(cart ->
                CartSessionUtils.overwriteCartSessionData(cart, session, userContext, reverseRouter()), HttpExecution.defaultContext());
        return cartFuture;
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
        final CartQueryBuilder queryBuilder = CartQueryBuilder.of()
                .plusPredicates(cart -> cart.is(Cart.referenceOfId(cartId)));
        return queryCartOrNew(queryBuilder, userContext);
    }

    protected CompletionStage<Cart> fetchCartByCustomerOrNew(final String customerId, final UserContext userContext) {
        final CartQueryBuilder queryBuilder = CartQueryBuilder.of()
                .plusPredicates(cart -> cart.customerId().is(customerId));
        return queryCartOrNew(queryBuilder, userContext);
    }

    protected CompletionStage<Cart> queryCartOrNew(final CartQueryBuilder queryBuilder, final UserContext userContext) {
        final CartQuery query = queryBuilder
                .plusPredicates(cart -> cart.cartState().is(CartState.ACTIVE))
                .plusExpansionPaths(c -> c.shippingInfo().shippingMethod()) // TODO pass as an optional parameter to avoid expanding always
                .plusExpansionPaths(c -> c.paymentInfo().payments())
                .sort(cart -> cart.lastModifiedAt().sort().desc())
                .limit(1)
                .build();
        return sphere().execute(query).thenComposeAsync(carts -> carts.head()
                .map(cart -> (CompletionStage<Cart>) completedFuture(cart))
                .orElseGet(() -> createCart(userContext)),
                HttpExecution.defaultContext());
    }

    protected CompletionStage<Cart> updateCartWithUserPreferences(final Cart cart, final UserContext userContext) {
        final boolean hasDifferentCountry = !userContext.country().equals(cart.getCountry());
        return hasDifferentCountry ? updateCartCountry(cart, userContext.country()) : completedFuture(cart);
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
