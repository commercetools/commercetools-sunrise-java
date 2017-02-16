package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.common.controllers.AbstractSphereRequestExecutor;
import com.commercetools.sunrise.common.sessions.carts.CartInSession;
import com.commercetools.sunrise.hooks.HookRunner;
import com.commercetools.sunrise.hooks.events.CartLoadedHook;
import com.commercetools.sunrise.hooks.requests.CartQueryHook;
import com.commercetools.sunrise.common.sessions.customers.CustomerInSession;
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

final class CartFinderBySession extends AbstractSphereRequestExecutor implements CartFinder {

    private final CartInSession cartInSession;
    private final CustomerInSession customerInSession;

    @Inject
    CartFinderBySession(final SphereClient sphereClient, final HookRunner hookRunner,
                        final CartInSession cartInSession, final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.cartInSession = cartInSession;
        this.customerInSession = customerInSession;
    }

    @Override
    public CompletionStage<Optional<Cart>> get() {
        final CompletionStage<Optional<Cart>> cartStage = fetchCart();
        cartStage.thenAcceptAsync(cartOpt ->
                cartOpt.ifPresent(cart -> CartLoadedHook.runHook(getHookRunner(), cart)), HttpExecution.defaultContext());
        return cartStage;
    }

    private CompletionStage<Optional<Cart>> fetchCart() {
        return buildQuery()
                .map(query -> CartQueryHook.runHook(getHookRunner(), query))
                .map(query -> getSphereClient().execute(query)
                        .thenApplyAsync(PagedResult::head, HttpExecution.defaultContext()))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    private Optional<CartQuery> buildQuery() {
        return tryBuildQueryByCustomerId()
                .map(Optional::of)
                .orElseGet(this::tryBuildQueryByCartId)
                .map(this::decorateQueryWithAdditionalInfo);
    }

    private Optional<CartQuery> tryBuildQueryByCartId() {
        return cartInSession.findCartId()
                .map(this::buildQueryByCartId);
    }

    private CartQuery buildQueryByCartId(final String cartId) {
        return CartQuery.of().plusPredicates(cart -> cart.id().is(cartId));
    }

    private Optional<CartQuery> tryBuildQueryByCustomerId() {
        return customerInSession.findCustomerId()
                .map(this::buildQueryByCustomerId);
    }

    private CartQuery buildQueryByCustomerId(final String customerId) {
        return CartQuery.of().plusPredicates(cart -> cart.customerId().is(customerId));
    }

    private CartQuery decorateQueryWithAdditionalInfo(final CartQuery query) {
        return query
                .plusPredicates(cart -> cart.cartState().is(CartState.ACTIVE))
                .plusExpansionPaths(c -> c.shippingInfo().shippingMethod()) // TODO use run hook on cart query instead
                .plusExpansionPaths(c -> c.paymentInfo().payments())
                .withSort(cart -> cart.lastModifiedAt().sort().desc())
                .withLimit(1);
    }
}
