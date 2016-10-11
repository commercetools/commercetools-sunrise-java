package com.commercetools.sunrise.shoppingcart.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.contexts.UserContext;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.common.reverserouter.HomeReverseRouter;
import com.commercetools.sunrise.hooks.actions.CartUpdatedActionHook;
import com.commercetools.sunrise.hooks.events.CartLoadedHook;
import com.commercetools.sunrise.hooks.events.CartUpdatedHook;
import com.commercetools.sunrise.hooks.requests.CartQueryHook;
import com.commercetools.sunrise.hooks.requests.CartUpdateCommandHook;
import com.commercetools.sunrise.myaccount.CustomerSessionHandler;
import com.commercetools.sunrise.shoppingcart.CartSessionHandler;
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
import org.slf4j.LoggerFactory;
import play.mvc.Http;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static io.sphere.sdk.utils.CompletableFutureUtils.successful;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

@NoCache
public abstract class SunriseFrameworkCartController extends SunriseFrameworkController {

    @Inject
    private void postInit() {
        //just prepend another error handler if this does not suffice
        prependErrorHandler(e -> e instanceof CartNotFoundException || e instanceof CartEmptyException, e -> {
            LoggerFactory.getLogger(SunriseFrameworkCartController.class).error("access denied", e);
            return successful(redirect(injector().getInstance(HomeReverseRouter.class).homePageCall(injector().getInstance(UserContext.class).languageTag())));
        });
    }

    protected CompletionStage<Cart> executeCartUpdateCommandWithHooks(final CartUpdateCommand cmd) {
        final CartUpdateCommand command = CartUpdateCommandHook.runHook(hooks(), cmd);
        final CompletionStage<Cart> cartAfterOriginalCommandStage = sphere().execute(command);
        return cartAfterOriginalCommandStage
                .thenComposeAsync(cart -> CartUpdatedActionHook.runHook(hooks(), cart, cmd), defaultContext())
                .thenApplyAsync(cart -> {
                    CartUpdatedHook.runHook(hooks(), cart);
                    return cart;
                }, defaultContext());
    }

    /**
     * Searches for an existing cart the platform otherwise the stage contains a {@link CartNotFoundException}.
     * A cart will not be created if it does not exist.
     * @return stage
     */
    protected CompletionStage<Cart> requiringExistingCart() {
        return findCart()
                .thenApplyAsync(cartOptional -> cartOptional.orElseThrow(() -> new CartNotFoundException()), defaultContext());
    }

    protected CompletionStage<Cart> requiringNonEmptyCart() {
        return requiringExistingCart().thenApplyAsync(cart -> {
            if (cart.getLineItems().isEmpty()) {
                throw new CartEmptyException(cart);
            } else {
                return cart;
            }
        }, defaultContext());
    }

    /**
     * Loads the primary cart from commercetools platform without applying side effects like updating the cart or the session.
     * @return a future of the optional cart
     */
    protected CompletionStage<Optional<Cart>> findCart() {
        final Http.Session session = session();
        return injector().getInstance(CustomerSessionHandler.class).findCustomerId(session)
                .map(customerId -> CartQuery.of().plusPredicates(cart -> cart.customerId().is(customerId)))
                .map(Optional::of)
                .orElseGet(() -> injector().getInstance(CartSessionHandler.class).findCartId(session)
                        .map(cartId -> CartQuery.of().plusPredicates(cart -> cart.id().is(cartId))))
                .map(query -> query.plusPredicates(cart -> cart.cartState().is(CartState.ACTIVE))
                        .plusExpansionPaths(c -> c.shippingInfo().shippingMethod()) // TODO pass as an optional parameter to avoid expanding always
                        .plusExpansionPaths(c -> c.paymentInfo().payments())
                        .withSort(cart -> cart.lastModifiedAt().sort().desc())
                        .withLimit(1))
                .map(query -> CartQueryHook.runHook(hooks(), query))
                .map(query -> sphere().execute(query).thenApplyAsync(PagedResult::head, defaultContext()))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    protected CompletionStage<Cart> getOrCreateCart() {
        final CompletionStage<Cart> cartCompletionStage = findCart()
                .thenComposeAsync(cartOptional -> cartOptional
                                .map(cart -> (CompletionStage<Cart>) completedFuture(cart))
                                .orElseGet(() -> createCart(userContext())),
                        defaultContext());
        return cartCompletionStage.thenComposeAsync(cart -> applySideEffects(cart), defaultContext());

    }

    protected CompletionStage<Cart> applySideEffects(@Nonnull Cart cart) {
        return updateCartWithUserPreferences(cart, userContext())
                .thenApply(updatedCart -> {
                    overwriteCartInSession(updatedCart);
                    CartLoadedHook.runHook(hooks(), updatedCart);
                    return updatedCart;
                });
    }

    protected CompletionStage<List<ShippingMethod>> getShippingMethods() {
        return injector().getInstance(CartSessionHandler.class).findCartId(session())
                .map(cartId -> sphere().execute(ShippingMethodsByCartGet.of(cartId)))
                .orElseGet(() -> completedFuture(emptyList()));
    }

    protected CompletionStage<Cart> createCart(final UserContext userContext) {
        final Address address = Address.of(userContext.country());
        final CustomerSessionHandler customerSessionHandler = injector().getInstance(CustomerSessionHandler.class);
        final Http.Session session = session();
        final CartDraft cartDraft = CartDraftBuilder.of(userContext.currency())
                .country(address.getCountry())
                .shippingAddress(address)
                .customerId(customerSessionHandler.findCustomerId(session).orElse(null))
                .customerEmail(customerSessionHandler.findCustomerEmail(session).orElse(null))
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
}
