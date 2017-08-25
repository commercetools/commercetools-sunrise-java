package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.sessions.customer.CustomerInSession;
import com.commercetools.sunrise.sessions.wishlist.ShoppingListsInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

class ShoppingListFinderBySession extends AbstractSingleShoppingListQueryExecutor implements ShoppingListFinder {
    private final ShoppingListsInSession shoppingListsInSession;
    private final CustomerInSession customerInSession;

    @Inject
    protected ShoppingListFinderBySession(final SphereClient sphereClient, final HookRunner hookRunner,
                                          final ShoppingListsInSession shoppingListsInSession, final CustomerInSession customerInSession) {
        super(sphereClient, hookRunner);
        this.shoppingListsInSession = shoppingListsInSession;
        this.customerInSession = customerInSession;
    }

    @Override
    public CompletionStage<Optional<ShoppingList>> get(final String shoppinglistType) {
        return buildQuery(shoppinglistType)
                .map(this::executeRequest)
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    private Optional<ShoppingListQuery> buildQuery(final String shoppinglistType) {
        return tryBuildQueryByCustomerId()
                .map(Optional::of)
                .orElse(tryBuildQueryByWishlistId(shoppinglistType))
                .map(this::decorateQueryWithAdditionalInfo);
    }

    private Optional<ShoppingListQuery> tryBuildQueryByCustomerId() {
        return customerInSession.findCustomerId()
                .map(customerId -> ShoppingListQuery.of()
                        .withExpansionPaths(m -> m.lineItems().productSlug())
                        .plusPredicates(m -> m.customer().id().is(customerId)));
    }

    private Optional<ShoppingListQuery> tryBuildQueryByWishlistId(final String shoppinglistType) {
        return shoppingListsInSession.findShoppingListId( shoppinglistType )
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
