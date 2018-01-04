package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.sessions.AbstractResourceInCache;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.cache.CacheApi;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@RequestScoped
final class WishlistInCacheImpl extends AbstractResourceInCache<ShoppingList> implements WishlistInCache {

    private final WishlistFinder wishlistFinder;

    @Inject
    WishlistInCacheImpl(final WishlistInSession wishlistInSession, final CacheApi cacheApi, final WishlistFinder wishlistFinder) {
        super(wishlistInSession, cacheApi);
        this.wishlistFinder = wishlistFinder;
    }

    @Override
    protected CompletionStage<Optional<ShoppingList>> fetchResource() {
        return wishlistFinder.get();
    }

    @Override
    protected String generateCacheKey(final String wishlistId) {
        return "wishlist_" + wishlistId;
    }
}
