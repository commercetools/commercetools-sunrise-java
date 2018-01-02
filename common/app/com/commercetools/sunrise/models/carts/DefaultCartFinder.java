package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.hooks.HookRunner;
import com.commercetools.sunrise.models.customers.CustomerInSession;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.CartState;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.client.SphereClient;
import play.cache.CacheApi;
import play.libs.concurrent.HttpExecution;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public class DefaultCartFinder extends AbstractSingleCartQueryExecutor implements CartFinder {

    private final CartInSession cartInSession;
    private final CustomerInSession customerInSession;
    private final CacheApi cacheApi;

    @Inject
    protected DefaultCartFinder(final SphereClient sphereClient, final HookRunner hookRunner,
                                final CartInSession cartInSession, final CustomerInSession customerInSession,
                                final CacheApi cacheApi) {
        super(sphereClient, hookRunner);
        this.cartInSession = cartInSession;
        this.customerInSession = customerInSession;
        this.cacheApi = cacheApi;
    }

    protected final CartInSession getCartInSession() {
        return cartInSession;
    }

    protected final CustomerInSession getCustomerInSession() {
        return customerInSession;
    }

    protected final CacheApi getCacheApi() {
        return cacheApi;
    }

    @Override
    public CompletionStage<Optional<Cart>> get() {
        return generateCacheKey().map(this::findInCacheOrFetch)
                .orElseGet(() -> completedFuture(Optional.empty()));
    }

    protected CompletionStage<Optional<CartQuery>> buildRequest() {
        final Optional<CartQuery> cartQuery = tryBuildQueryByCustomerId()
                .map(Optional::of)
                .orElseGet(this::tryBuildQueryByCartId)
                .map(this::decorateQueryWithAdditionalInfo);
        return completedFuture(cartQuery);
    }

    protected final Optional<String> generateCacheKey() {
        return cartInSession.findCartId().map(cartId -> "cart_" + cartId);
    }

    private CompletionStage<Optional<Cart>> findInCacheOrFetch(final String cacheKey) {
        final Cart nullableCart = cacheApi.get(cacheKey);
        return Optional.ofNullable(nullableCart)
                .map(cart -> (CompletionStage<Optional<Cart>>) completedFuture(Optional.of(cart)))
                .orElseGet(() -> fetchAndStoreCart(cacheKey));
    }

    private CompletionStage<Optional<Cart>> fetchAndStoreCart(final String cacheKey) {
        final CompletionStage<Optional<Cart>> cartStage = fetchCart();
        cartStage.thenAcceptAsync(cartOpt ->
                cartOpt.ifPresent(cart -> cacheApi.set(cacheKey, cart)),
                HttpExecution.defaultContext());
        return cartStage;
    }

    private CompletionStage<Optional<Cart>> fetchCart() {
        return buildRequest().thenComposeAsync(request ->
                request.map(this::executeRequest).orElseGet(() -> completedFuture(Optional.empty())),
                HttpExecution.defaultContext());
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
