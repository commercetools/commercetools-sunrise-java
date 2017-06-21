package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.sessions.cart.CartInSession;
import com.commercetools.sunrise.sessions.customer.CustomerInSession;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartState;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

final class CartFinderBySession extends AbstractSingleCartQueryExecutor implements CartFinder {

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
        return buildQuery()
                .map(this::executeRequest)
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    private Optional<CartQuery> buildQuery() {
        return tryBuildQueryByCustomerId()
                .map(Optional::of)
                .orElseGet(this::tryBuildQueryByCartId)
                .map(this::decorateQueryWithAdditionalInfo);
    }

    private Optional<CartQuery> tryBuildQueryByCustomerId() {
        return customerInSession.findCustomerId()
                .map(customerId -> CartQuery.of().plusPredicates(cart -> cart.customerId().is(customerId)));
    }

    private Optional<CartQuery> tryBuildQueryByCartId() {
        return cartInSession.findCartId()
                .map(cartId -> CartQuery.of().plusPredicates(cart -> cart.id().is(cartId)));
    }

    private CartQuery decorateQueryWithAdditionalInfo(final CartQuery query) {
        return query
                .plusPredicates(cart -> cart.cartState().is(CartState.ACTIVE))
                .withSort(cart -> cart.lastModifiedAt().sort().desc())
                .withLimit(1);
    }
}
