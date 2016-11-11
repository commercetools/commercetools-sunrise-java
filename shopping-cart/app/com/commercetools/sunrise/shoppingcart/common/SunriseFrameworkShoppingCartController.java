package com.commercetools.sunrise.shoppingcart.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.hooks.actions.CartUpdatedActionHook;
import com.commercetools.sunrise.hooks.events.CartCreatedHook;
import com.commercetools.sunrise.hooks.events.CartUpdatedHook;
import com.commercetools.sunrise.hooks.requests.CartCreateCommandHook;
import com.commercetools.sunrise.hooks.requests.CartUpdateCommandHook;
import com.commercetools.sunrise.shoppingcart.CartFinderBySession;
import com.commercetools.sunrise.shoppingcart.CartInSession;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodsByCartGet;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;
import static play.libs.concurrent.HttpExecution.defaultContext;

@NoCache
public abstract class SunriseFrameworkShoppingCartController extends SunriseFrameworkController {

    @Inject
    private void postInit() {
        //just prepend another error handler if this does not suffice
        prependErrorHandler(e -> e instanceof CartNotFoundException || e instanceof CartEmptyException, e -> {
            LoggerFactory.getLogger(SunriseFrameworkShoppingCartController.class).error("access denied", e);
            return redirectToHome();
        });
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("shopping-cart"));
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

    protected CompletionStage<Optional<Cart>> findCart() {
        return injector().getInstance(CartFinderBySession.class).findCart(null);
    }

    /**
     * Searches for an existing cart the platform otherwise the stage contains a {@link CartNotFoundException}.
     * A cart will not be created if it does not exist.
     * @return stage with the cart in session, or {@link CartNotFoundException}
     */
    protected CompletionStage<Cart> requireExistingCart() {
        return findCart()
                .thenApplyAsync(cartOptional -> cartOptional
                        .orElseThrow(CartNotFoundException::new), defaultContext());
    }

    protected CompletionStage<Cart> requireNonEmptyCart() {
        return requireExistingCart()
                .thenApplyAsync(cart -> {
                    if (cart.getLineItems().isEmpty()) {
                        throw new CartEmptyException(cart);
                    } else {
                        return cart;
                    }
                }, defaultContext());
    }

    protected CompletionStage<Cart> createCart() {
        final CartDraft cartDraft = CartDraftBuilder.of(userContext().currency()).build();
        final CartCreateCommand cartCreateCommand = CartCreateCommandHook.runHook(hooks(), CartCreateCommand.of(cartDraft));
        final CompletionStage<Cart> cartStage = sphere().execute(cartCreateCommand);
        cartStage.thenAcceptAsync(cart -> CartCreatedHook.runHook(hooks(), cart), defaultContext());
        return cartStage;
    }

    protected CompletionStage<List<ShippingMethod>> getShippingMethods() {
        return injector().getInstance(CartInSession.class).findCartId()
                .map(cartId -> sphere().execute(ShippingMethodsByCartGet.of(cartId)))
                .orElseGet(() -> completedFuture(emptyList()));
    }
}
