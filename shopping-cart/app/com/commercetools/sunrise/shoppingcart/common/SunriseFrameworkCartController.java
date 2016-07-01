package com.commercetools.sunrise.shoppingcart.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.reverserouter.ProductReverseRouter;
import com.commercetools.sunrise.hooks.CartQueryFilterHook;
import com.commercetools.sunrise.hooks.PrimaryCartLoadedHook;
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
import io.sphere.sdk.models.Address;
import io.sphere.sdk.queries.PagedResult;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodsByCartGet;
import play.mvc.Http;

import javax.annotation.Nonnull;
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

    /**
     * Searches for an existing cart the platform otherwise the stage contains a {@link PrimaryCartNotFoundException}.
     * A cart will not be created if it does not exist.
     * @return stage
     */
    protected CompletionStage<Cart> requiringExistingPrimaryCart() {
        return findPrimaryCartInCommercetoolsPlatform()
                .thenApplyAsync(cartOptional -> cartOptional.orElseThrow(() -> new PrimaryCartNotFoundException()), defaultContext());
    }

    /**
     * Loads the primary cart from commercetools platform without applying side effects like updating the cart or the session.
     * @return a future of the optional cart
     */
    protected CompletionStage<Optional<Cart>> findPrimaryCartInCommercetoolsPlatform() {
        final CartQuery query = buildQueryForPrimaryCart(session());
        return sphere().execute(query).thenApplyAsync(PagedResult::head, defaultContext());
    }


    protected CompletionStage<Cart> getOrCreateCart() {
        final CompletionStage<Cart> cartCompletionStage = findPrimaryCartInCommercetoolsPlatform()
                .thenComposeAsync(cartOptional -> cartOptional
                                .map(cart -> (CompletionStage<Cart>) completedFuture(cart))
                                .orElseGet(() -> createCart(userContext())),
                        defaultContext());
        return cartCompletionStage.thenComposeAsync(cart -> applySideEffects(cart), defaultContext());

    }

    protected CompletionStage<Cart> applySideEffects(@Nonnull Cart cart) {
        final Http.Session session = session();
        return updateCartWithUserPreferences(cart, userContext()).thenApply(updatedCart -> {
            CartSessionUtils.overwriteCartSessionData(updatedCart, session, userContext(), productReverseRouter);
            return updatedCart;
        })
                .thenApply(updatedCart -> {
                    hooks().runAsyncHook(PrimaryCartLoadedHook.class, hook -> hook.onUserCartLoaded(updatedCart));
                    return updatedCart;
        });
    }

    protected CompletionStage<List<ShippingMethod>> getShippingMethods() {
        return CartSessionUtils.getCartId(session())
                .map(cartId -> sphere().execute(ShippingMethodsByCartGet.of(cartId)))
                .orElseGet(() -> completedFuture(emptyList()));
    }

    private CartQuery buildQueryForPrimaryCart(final Http.Session session) {
        final String nullableCustomerId = CustomerSessionUtils.getCustomerId(session).orElse(null);
        final String nullableCartId = CartSessionUtils.getCartId(session).orElse(null);
        CartQuery query = CartQuery.of();
        if (nullableCustomerId != null) {
            query = query.plusPredicates(cart -> cart.customerId().is(nullableCustomerId));
        } else if(nullableCartId != null) {
            query = query.plusPredicates(cart -> cart.id().is(nullableCartId));
        }
        query = query
                .plusPredicates(cart -> cart.cartState().is(CartState.ACTIVE))
                .plusExpansionPaths(c -> c.shippingInfo().shippingMethod()) // TODO pass as an optional parameter to avoid expanding always
                .plusExpansionPaths(c -> c.paymentInfo().payments())
                .withSort(cart -> cart.lastModifiedAt().sort().desc())
                .withLimit(1);
        query = hooks().runFilterHook(CartQueryFilterHook.class, (hook, q) -> hook.filterCartQuery(q), query);
        return query;
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
