package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.CustomerInSession;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;
import play.cache.CacheApi;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultWishlistFinder extends AbstractSingleShoppingListQueryExecutor implements WishlistFinder {

    private final WishlistInSession wishlistInSession;
    private final CustomerInSession customerInSession;
    private final CacheApi cacheApi;

    @Inject
    protected DefaultWishlistFinder(final SphereClient sphereClient, final HookRunner hookRunner,
                                    final WishlistInSession wishlistInSession, final CustomerInSession customerInSession,
                                    final CacheApi cacheApi) {
        super(sphereClient, hookRunner);
        this.wishlistInSession = wishlistInSession;
        this.customerInSession = customerInSession;
        this.cacheApi = cacheApi;
    }

    @Override
    public CompletionStage<Optional<ShoppingList>> get() {
        return generateCacheKey().map(this::findInCacheOrFetch)
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    protected CompletionStage<Optional<ShoppingListQuery>> buildRequest() {
        final Optional<ShoppingListQuery> query = tryBuildQueryByCustomerId()
                .map(Optional::of)
                .orElseGet(this::tryBuildQueryByWishlistId)
                .map(this::decorateQueryWithAdditionalInfo);
        return completedFuture(query);
    }

    protected final Optional<String> generateCacheKey() {
        return wishlistInSession.findWishlistId().map(wishlistId -> "whislist_" + wishlistId);
    }

    private CompletionStage<Optional<ShoppingList>> findInCacheOrFetch(final String cacheKey) {
        final ShoppingList nullableWishlist = cacheApi.get(cacheKey);
        return Optional.ofNullable(nullableWishlist)
                .map(wishlist -> (CompletionStage<Optional<ShoppingList>>) completedFuture(Optional.of(wishlist)))
                .orElseGet(() -> fetchAndStoreWishlist(cacheKey));
    }

    private CompletionStage<Optional<ShoppingList>> fetchAndStoreWishlist(final String cacheKey) {
        final CompletionStage<Optional<ShoppingList>> wishlistStage = fetchWishlist();
        wishlistStage.thenAcceptAsync(wishlistOpt ->
                        wishlistOpt.ifPresent(wishlist -> cacheApi.set(cacheKey, wishlist)),
                HttpExecution.defaultContext());
        return wishlistStage;
    }

    private CompletionStage<Optional<ShoppingList>> fetchWishlist() {
        return buildRequest().thenComposeAsync(request ->
                        request.map(this::executeRequest).orElseGet(() -> completedFuture(Optional.empty())),
                HttpExecution.defaultContext());
    }

    private Optional<ShoppingListQuery> tryBuildQueryByCustomerId() {
        return customerInSession.findCustomerId()
                .map(customerId -> ShoppingListQuery.of()
                        .withExpansionPaths(m -> m.lineItems().productSlug())
                        .plusPredicates(m -> m.customer().id().is(customerId)));
    }

    private Optional<ShoppingListQuery> tryBuildQueryByWishlistId() {
        return wishlistInSession.findWishlistId()
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
