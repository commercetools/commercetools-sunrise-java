package com.commercetools.sunrise.shoppingcart.common;

import com.commercetools.sunrise.common.cache.NoCache;
import com.commercetools.sunrise.common.controllers.SunriseFrameworkController;
import com.commercetools.sunrise.hooks.actions.CartUpdatedActionHook;
import com.commercetools.sunrise.hooks.events.CartCreatedHook;
import com.commercetools.sunrise.hooks.events.CartUpdatedHook;
import com.commercetools.sunrise.hooks.requests.CartCreateCommandHook;
import com.commercetools.sunrise.hooks.requests.CartUpdateCommandHook;
import com.commercetools.sunrise.shoppingcart.CartFinder;
import com.commercetools.sunrise.shoppingcart.CartFinderBySession;
import com.commercetools.sunrise.shoppingcart.CartInSession;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartDraft;
import io.sphere.sdk.carts.CartDraftBuilder;
import io.sphere.sdk.carts.commands.CartCreateCommand;
import io.sphere.sdk.carts.commands.CartUpdateCommand;
import io.sphere.sdk.customers.Customer;
import io.sphere.sdk.shippingmethods.ShippingMethod;
import io.sphere.sdk.shippingmethods.queries.ShippingMethodsByCartGet;
import org.slf4j.LoggerFactory;
import play.libs.concurrent.HttpExecution;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.concurrent.CompletableFuture.completedFuture;

@NoCache
public abstract class SunriseFrameworkShoppingCartController extends SunriseFrameworkController {

    private final CartFinder cartFinder;

    protected SunriseFrameworkShoppingCartController(final CartFinder cartFinder) {
        this.cartFinder = cartFinder;
    }

    @Override
    public Set<String> getFrameworkTags() {
        return new HashSet<>(asList("shopping-cart"));
    }

    protected final CompletionStage<Result> requireNonEmptyCart(final Function<Cart, CompletionStage<Result>> nextAction) {
        return cartFinder.get()
                .thenComposeAsync(cartOpt -> cartOpt
                                .filter(cart -> !cart.getLineItems().isEmpty())
                                .map(nextAction)
                                .orElseGet(this::handleNotFoundCart),
                        HttpExecution.defaultContext());
    }

    protected abstract CompletionStage<Result> handleNotFoundCart();

    protected CompletionStage<Cart> executeCartUpdateCommandWithHooks(final CartUpdateCommand cmd) {
        final CartUpdateCommand command = CartUpdateCommandHook.runHook(hooks(), cmd);
        final CompletionStage<Cart> cartAfterOriginalCommandStage = sphere().execute(command);
        return cartAfterOriginalCommandStage
                .thenComposeAsync(cart -> CartUpdatedActionHook.runHook(hooks(), cart, cmd), HttpExecution.defaultContext())
                .thenApplyAsync(cart -> {
                    CartUpdatedHook.runHook(hooks(), cart);
                    return cart;
                }, HttpExecution.defaultContext());
    }

    protected CompletionStage<List<ShippingMethod>> getShippingMethods() {
        return injector().getInstance(CartInSession.class).findCartId()
                .map(cartId -> sphere().execute(ShippingMethodsByCartGet.of(cartId)))
                .orElseGet(() -> completedFuture(emptyList()));
    }
}
