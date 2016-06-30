package com.commercetools.sunrise.shoppingcart.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.hooks.UserCartLoadedHook;
import com.commercetools.sunrise.myaccount.CustomerSessionUtils;
import com.commercetools.sunrise.shoppingcart.CartSessionUtils;
import com.google.inject.Inject;
import com.neovisionaries.i18n.CountryCode;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.CartState;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.carts.commands.updateactions.SetCountry;
import io.sphere.sdk.carts.commands.updateactions.SetShippingAddress;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.carts.queries.CartQueryBuilder;
import io.sphere.sdk.models.Address;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodsByCartGet;
import play.mvc.Http;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static com.commercetools.sunrise.shoppingcart.CartSessionUtils.overwriteCartSessionData;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

@NoCache
public abstract class SunriseFrameworkCartController extends SunriseFrameworkController {

    @Inject
    private ProductReverseRouter productReverseRouter;

    protected CompletionStage<Cart> getOrCreateCart() {
        final UserContext userContext = userContext();
        final Http.Session session = session();
        return fetchCart(userContext, session)
                .thenComposeAsync(cart -> updateCartWithUserPreferences(cart, userContext), defaultContext())
                .thenApply(cart -> {
                    CartSessionUtils.overwriteCartSessionData(cart, session, userContext, productReverseRouter);
                    return cart;
                })
                .thenApply(cart -> {
                    hooks().runAsyncHook(UserCartLoadedHook.class, hook -> hook.onUserCartLoaded(cart));
                    return cart;
                });
    }

    protected CompletionStage<List<ShippingMethod>> getShippingMethods() {
        return CartSessionUtils.getCartId(session())
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
                defaultContext());
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

    protected void overrideCartSessionData(final Cart cart) {
        overwriteCartSessionData(cart, session(), userContext(), productReverseRouter);
    }
}
