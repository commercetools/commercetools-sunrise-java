package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.CustomerInSession;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartState;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultCartFinder extends AbstractSingleCartQueryExecutor implements CartFinder {

    private final CartInSession cartInSession;
    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultCartFinder(final SphereClient sphereClient, final HookRunner hookRunner,
                                final CartInSession cartInSession, final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.cartInSession = cartInSession;
        this.customerInSession = customerInSession;
    }

    protected final CartInSession getCartInSession() {
        return cartInSession;
    }

    protected final CustomerInSession getCustomerInSession() {
        return customerInSession;
    }

    @Override
    public CompletionStage<Optional<Cart>> get() {
        return buildRequest().map(this::executeRequest).orElseGet(() -> completedFuture(Optional.empty()));
    }

    protected Optional<CartQuery> buildRequest() {
        return tryBuildQueryByCustomerId()
                .map(Optional::of)
                .orElseGet(this::tryBuildQueryByCartId)
                .map(this::decorateQueryWithAdditionalInfo);
    }

    private Optional<CartQuery> tryBuildQueryByCustomerId() {
        return customerInSession.findId()
                .map(customerId -> CartQuery.of().plusPredicates(cart -> cart.customerId().is(customerId)));
    }

    private Optional<CartQuery> tryBuildQueryByCartId() {
        return cartInSession.findId()
                .map(cartId -> CartQuery.of().plusPredicates(cart -> cart.id().is(cartId)));
    }

    private CartQuery decorateQueryWithAdditionalInfo(final CartQuery query) {
        return query
                .plusPredicates(cart -> cart.cartState().is(CartState.ACTIVE))
                .withSort(cart -> cart.lastModifiedAt().sort().desc())
                .withLimit(1);
    }
}
