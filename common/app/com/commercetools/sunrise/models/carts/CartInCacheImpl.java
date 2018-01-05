package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.sessions.AbstractResourceInCache;
import io.sphere.sdk.carts.Cart;
import play.cache.CacheApi;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@RequestScoped
final class CartInCacheImpl extends AbstractResourceInCache<Cart> implements CartInCache {

    private final CartFetcher cartFetcher;

    @Inject
    CartInCacheImpl(final CartInSession cartInSession, final CacheApi cacheApi, final CartFetcher cartFetcher) {
        super(cartInSession, cacheApi);
        this.cartFetcher = cartFetcher;
    }

    @Override
    protected CompletionStage<Optional<Cart>> fetchResource() {
        return cartFetcher.get();
    }

    @Override
    protected String generateCacheKey(final String cartId) {
        return "cart_" + cartId;
    }
}
