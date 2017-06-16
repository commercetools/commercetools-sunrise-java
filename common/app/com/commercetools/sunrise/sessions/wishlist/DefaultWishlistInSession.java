package com.commercetools.sunrise.sessions.wishlist;

import com.commercetools.sunrise.framework.viewmodels.content.wishlist.WishlistViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.wishlist.WishlistViewModelFactory;
import com.commercetools.sunrise.sessions.DataFromResourceStoringOperations;
import com.commercetools.sunrise.sessions.ObjectStoringSessionStrategy;
import io.sphere.sdk.shoppinglists.ShoppingList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import play.Configuration;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * Default implementation of {@link WishlistInSession}.
 */
public class DefaultWishlistInSession extends DataFromResourceStoringOperations<ShoppingList> implements  WishlistInSession {
    private static final Logger LOGGER = LoggerFactory.getLogger(WishlistInSession.class);
    private static final String DEFAULT_WISHLIST_ID_SESSION_KEY = "sunrise-wishlist-id";
    private static final String DEFAULT_WISHLIST_SESSION_KEY = "sunrise-wishlist";

    private final String wishlistIdSessionKey;
    private final String wishlistSessionKey;
    private final ObjectStoringSessionStrategy session;
    private final WishlistViewModelFactory wishlistViewModelFactory;

    @Inject
    protected DefaultWishlistInSession(final Configuration configuration, final ObjectStoringSessionStrategy session,
                                    final WishlistViewModelFactory wishlistViewModelFactory) {
        this.wishlistIdSessionKey = configuration.getString("session.wishlist.wishlistId", DEFAULT_WISHLIST_ID_SESSION_KEY);
        this.wishlistSessionKey = configuration.getString("session.wishlist.wishlist", DEFAULT_WISHLIST_SESSION_KEY);
        this.session = session;
        this.wishlistViewModelFactory = wishlistViewModelFactory;
    }

    @Override
    public Optional<String> findWishlistId() {
        return session.findValueByKey(wishlistIdSessionKey);
    }

    @Override
    public Optional<WishlistViewModel> findWishlist() {
        return session.findObjectByKey(wishlistSessionKey, WishlistViewModel.class);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected void storeAssociatedData(@NotNull final ShoppingList wishlist) {
        session.overwriteValueByKey(wishlistIdSessionKey, wishlist.getId());
        session.overwriteObjectByKey(wishlistSessionKey, wishlistViewModelFactory.create(wishlist));
    }

    @Override
    protected void removeAssociatedData() {
        session.removeValueByKey(wishlistIdSessionKey);
        session.removeObjectByKey(wishlistSessionKey);
    }
}
