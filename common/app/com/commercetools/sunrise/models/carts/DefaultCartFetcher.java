package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.CustomerInSession;
import io.sphere.sdk.carts.CartState;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.client.SphereClient;

import javax.inject.Inject;
import java.util.Optional;

public class DefaultCartFetcher extends AbstractCartFetcher {

    private final MyCartInSession myCartInSession;
    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultCartFetcher(final SphereClient sphereClient, final HookRunner hookRunner,
                                 final MyCartInSession myCartInSession, final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.myCartInSession = myCartInSession;
        this.customerInSession = customerInSession;
    }

    protected final MyCartInSession getMyCartInSession() {
        return myCartInSession;
    }

    protected final CustomerInSession getCustomerInSession() {
        return customerInSession;
    }

    @Override
    public Optional<CartQuery> defaultRequest() {
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
        return myCartInSession.findId()
                .map(cartId -> CartQuery.of().plusPredicates(cart -> cart.id().is(cartId)));
    }

    private CartQuery decorateQueryWithAdditionalInfo(final CartQuery query) {
        return query
                .plusPredicates(cart -> cart.cartState().is(CartState.ACTIVE))
                .withSort(cart -> cart.lastModifiedAt().sort().desc())
                .withLimit(1);
    }
}
