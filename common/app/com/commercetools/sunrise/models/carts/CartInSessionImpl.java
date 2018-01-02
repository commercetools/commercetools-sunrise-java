package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.sessions.StoringStrategy;
import io.sphere.sdk.carts.Cart;
import play.Configuration;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

/**
 * Keeps some parts from the cart in session, such as cart ID and mini cart.
 */
@Singleton
final class CartInSessionImpl implements CartInSession {

    private final String cookieName;
    private final boolean cookieSecure;
    private final boolean cookieHttpOnly;
    private final StoringStrategy storingStrategy;

    @Inject
    CartInSessionImpl(final Configuration globalConfig, final StoringStrategy storingStrategy) {
        final Configuration config = globalConfig.getConfig("sunrise.carts");
        this.cookieName = config.getString("cookieName");
        this.cookieSecure = config.getBoolean("cookieHttpOnly");
        this.cookieHttpOnly = config.getBoolean("cookieHttpOnly");
        this.storingStrategy = storingStrategy;
    }

    @Override
    public Optional<String> findCartId() {
        return storingStrategy.findInCookies(cookieName);
    }

    @Override
    public void store(@Nullable final Cart cart) {
        final String cartId = cart != null ? cart.getId() : null;
        storingStrategy.overwriteInCookies(cookieName, cartId, cookieHttpOnly, cookieSecure);
    }

    @Override
    public void remove() {
        storingStrategy.removeFromCookies(cookieName);
    }
}
