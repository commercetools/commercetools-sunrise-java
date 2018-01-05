package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.CustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import javax.inject.Inject;
import java.util.Optional;

public class DefaultWishlistFetcher extends AbstractWishlistFetcher {

    private final WishlistInSession wishlistInSession;
    private final CustomerInSession customerInSession;

    @Inject
    protected DefaultWishlistFetcher(final SphereClient sphereClient, final HookRunner hookRunner,
                                     final WishlistInSession wishlistInSession, final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.wishlistInSession = wishlistInSession;
        this.customerInSession = customerInSession;
    }

    @Override
    protected Optional<ShoppingListQuery> buildRequest() {
        return tryBuildQueryByCustomerId()
                .map(Optional::of)
                .orElseGet(this::tryBuildQueryByWishlistId)
                .map(this::decorateQueryWithAdditionalInfo);
    }

    private Optional<ShoppingListQuery> tryBuildQueryByCustomerId() {
        return customerInSession.findId()
                .map(customerId -> ShoppingListQuery.of()
                        .withExpansionPaths(m -> m.lineItems().productSlug())
                        .plusPredicates(m -> m.customer().id().is(customerId)));
    }

    private Optional<ShoppingListQuery> tryBuildQueryByWishlistId() {
        return wishlistInSession.findId()
                .map(shoppingListId -> ShoppingListQuery.of()
                        .plusPredicates(m -> m.id().is(shoppingListId)));
    }

    private ShoppingListQuery decorateQueryWithAdditionalInfo(final ShoppingListQuery query) {
        return query
                .withExpansionPaths(m -> m.lineItems().variant())
                .plusExpansionPaths(m -> m.lineItems().productSlug())
                .withSort(m -> m.createdAt().sort().desc())
                .withLimit(1);
    }
}
