package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.hooks.RequestHookContext;
import com.commercetools.sunrise.hooks.events.CartLoadedHook;
import com.commercetools.sunrise.hooks.requests.CartQueryHook;
import com.commercetools.sunrise.myaccount.CustomerInSession;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartState;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedResult;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class CartFinderBySession implements CartFinder<Void> {

    @Inject
    private SphereClient sphereClient;
    @Inject
    private CartInSession cartInSession;
    @Inject
    private CustomerInSession customerInSession;
    @Inject
    private RequestHookContext hookContext;

    @Override
    public CompletionStage<Optional<Cart>> findCart(final Void unused) {
        final CompletionStage<Optional<Cart>> cartStage = fetchCart();
        cartStage.thenAcceptAsync(cartOpt ->
                cartOpt.ifPresent(cart -> CartLoadedHook.runHook(hookContext, cart)), HttpExecution.defaultContext());
        return cartStage;
    }

    private CompletionStage<Optional<Cart>> fetchCart() {
        return buildQuery()
                .map(query -> CartQueryHook.runHook(hookContext, query))
                .map(query -> sphereClient.execute(query)
                        .thenApplyAsync(PagedResult::head, HttpExecution.defaultContext()))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    private Optional<CartQuery> buildQuery() {
        return customerInSession.findCustomerId()
                .map(customerId -> Optional.of(buildQueryByCustomerId(customerId)))
                .orElseGet(() -> cartInSession.findCartId()
                        .map(this::buildQueryByCartId))
                .map(query -> query
                        .plusPredicates(cart -> cart.cartState().is(CartState.ACTIVE))
                        .plusExpansionPaths(c -> c.shippingInfo().shippingMethod()) // TODO use run hook on cart query instead
                        .plusExpansionPaths(c -> c.paymentInfo().payments())
                        .withSort(cart -> cart.lastModifiedAt().sort().desc())
                        .withLimit(1));
    }

    private CartQuery buildQueryByCartId(final String cartId) {
        return CartQuery.of().plusPredicates(cart -> cart.id().is(cartId));
    }

    private CartQuery buildQueryByCustomerId(final String customerId) {
        return CartQuery.of().plusPredicates(cart -> cart.customerId().is(customerId));
    }
}
