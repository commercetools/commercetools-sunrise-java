package com.commercetools.sunrise.sessions.wishlist;

import com.commercetools.sunrise.framework.viewmodels.content.wishlist.WishlistViewModel;
import com.commercetools.sunrise.sessions.ResourceStoringOperations;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * This interface describes the operations to store and retrieve parts of a wishlist in the session.
 */
@ImplementedBy(DefaultWishlistInSession.class)
public interface WishlistInSession extends ResourceStoringOperations<ShoppingList> {

    /**
     * Returns the wishlist ID stored in the current session.
     *
     * @see ShoppingList#getId()
     *
     * @return the optional wishlist ID
     */
    Optional<String> findWishlistId();

    Optional<WishlistViewModel> findWishlist();

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
