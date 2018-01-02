package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.sessions.StoringStrategy;
import io.sphere.sdk.shoppinglists.ShoppingList;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.util.Optional;

/**
 * Default implementation of {@link WishlistInSession}.
 */
final class WishlistInSessionImpl implements WishlistInSession {

    private final String cookieName;
    private final boolean cookieSecure;
    private final boolean cookieHttpOnly;
    private final StoringStrategy storingStrategy;

    @Inject
    WishlistInSessionImpl(final Configuration globalConfig, final StoringStrategy storingStrategy) {
        final Configuration config = globalConfig.getConfig("sunrise.wishlists");
        this.cookieName = config.getString("cookieName");
        this.cookieSecure = config.getBoolean("cookieHttpOnly");
        this.cookieHttpOnly = config.getBoolean("cookieHttpOnly");
        this.storingStrategy = storingStrategy;
    }

    @Override
    public Optional<String> findWishlistId() {
        return storingStrategy.findInCookies(cookieName);
    }

    @Override
    public void store(@Nullable final ShoppingList whislist) {
        final String wishlistId = whislist != null ? whislist.getId() : null;
        storingStrategy.overwriteInCookies(cookieName, wishlistId, cookieHttpOnly, cookieSecure);
    }

    @Override
    public void remove() {
        storingStrategy.removeFromCookies(cookieName);
    }
}
