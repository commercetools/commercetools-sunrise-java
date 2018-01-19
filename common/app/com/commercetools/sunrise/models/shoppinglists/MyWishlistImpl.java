package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.injection.RequestScoped;
import com.commercetools.sunrise.core.sessions.AbstractUserResourceInCache;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.cache.CacheApi;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@RequestScoped
final class MyWishlistImpl extends AbstractUserResourceInCache<ShoppingList> implements MyWishlist {

    private final MyWishlistFetcher wishlistFinder;

    @Inject
    MyWishlistImpl(final MyWishlistInSession myWishlistInSession, final CacheApi cacheApi, final MyWishlistFetcher wishlistFinder) {
        super(myWishlistInSession, cacheApi);
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
