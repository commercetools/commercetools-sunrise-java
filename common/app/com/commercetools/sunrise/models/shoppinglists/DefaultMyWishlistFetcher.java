package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.MyCustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

final class DefaultMyWishlistFetcher extends AbstractMyWishlistFetcher {

    private final SphereClient sphereClient;
    private final MyWishlistInSession myWishlistInSession;
    private final MyCustomerInSession myCustomerInSession;

    @Inject
    protected DefaultMyWishlistFetcher(final HookRunner hookRunner, final SphereClient sphereClient,
                                       final MyWishlistInSession myWishlistInSession, final MyCustomerInSession myCustomerInSession) {
        super(hookRunner);
        this.sphereClient = sphereClient;
        this.myWishlistInSession = myWishlistInSession;
        this.myCustomerInSession = myCustomerInSession;
    }

    @Override
    public CompletionStage<Optional<ShoppingList>> get() {
        return buildRequest()
                .map(request -> runHook(request, r -> sphereClient.execute(r).thenApply(PagedQueryResult::head)))
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    private Optional<ShoppingListQuery> buildRequest() {
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
