package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.sessions.ResourceInSession;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * This interface describes the operations to store and retrieve parts of a wishlist in the session.
 */
@ImplementedBy(WishlistInSessionImpl.class)
public interface WishlistInSession extends ResourceInSession<ShoppingList> {

    /**
     * Returns the wishlist ID stored in the current session.
     *
     * @see ShoppingList#getId()
     *
     * @return the optional wishlist ID
     */
    @Override
    Optional<String> findId();

    /**
     * Returns the wishlist version stored in the current session.
     *
     * @see ShoppingList#getVersion()
     *
     * @return the optional wishlist version
     */
    @Override
    Optional<Long> findVersion();

    /**
     * Stores the given wishlist in the current session.
     *
     * @param wishlist the wishlist to store
     */
    @Override
    void store(@Nullable final ShoppingList wishlist);

    /**
     * Removes the wishlist from the current session.
     */
    @Override
    void remove();
}
