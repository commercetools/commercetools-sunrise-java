package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.MyCustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import javax.inject.Inject;
import java.util.Optional;

public class DefaultMyWishlistFetcher extends AbstractShoppingListFetcher {

    private final MyWishlistInSession myWishlistInSession;
    private final MyCustomerInSession myCustomerInSession;

    @Inject
    protected DefaultMyWishlistFetcher(final SphereClient sphereClient, final HookRunner hookRunner,
                                       final MyWishlistInSession myWishlistInSession, final MyCustomerInSession myCustomerInSession) {
        super(sphereClient, hookRunner);
        this.myWishlistInSession = myWishlistInSession;
        this.myCustomerInSession = myCustomerInSession;
    }

    protected final MyWishlistInSession getMyWishlistInSession() {
        return myWishlistInSession;
    }

    protected final MyCustomerInSession getMyCustomerInSession() {
        return myCustomerInSession;
    }

    @Override
    public Optional<ShoppingListQuery> defaultRequest() {
        return tryBuildQueryByCustomerId()
                .map(Optional::of)
                .orElseGet(this::tryBuildQueryByWishlistId)
                .map(this::decorateQueryWithAdditionalInfo);
    }

    private Optional<ShoppingListQuery> tryBuildQueryByCustomerId() {
        return myCustomerInSession.findId()
                .map(customerId -> ShoppingListQuery.of()
                        .withExpansionPaths(m -> m.lineItems().productSlug())
                        .plusPredicates(m -> m.customer().id().is(customerId)));
    }

    private Optional<ShoppingListQuery> tryBuildQueryByWishlistId() {
        return myWishlistInSession.findId()
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
