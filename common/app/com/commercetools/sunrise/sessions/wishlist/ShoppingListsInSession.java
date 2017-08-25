package com.commercetools.sunrise.sessions.wishlist;

import com.commercetools.sunrise.framework.viewmodels.content.wishlist.ShoppinglistViewModel;
import com.commercetools.sunrise.sessions.ResourceStoringOperations;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * This interface describes the operations to store and retrieve parts of a wishlist in the session.
 */
@ImplementedBy(DefaultShoppingListInSession.class)
public interface ShoppingListsInSession extends ResourceStoringOperations<ShoppingList> {

    /**
     * Returns the shoppingList ID stored in the current session.
     *
     * @see io.sphere.sdk.shoppinglists.ShoppingList#getId()
     *
     * @return the optional shoppingList ID
     */
    Optional<String> findShoppingListId(String shoppingListType);

    Optional<ShoppinglistViewModel> findShoppingList(String shoppingListType);

    /**
     * Stores the given shoppingListContainer in the current session.
     *
     * @param shoppingList the shoppingListContainer to store
     */
    @Override
    void store(@Nullable final ShoppingList shoppingList);

    /**
     * Removes the wishlist from the current session.
     */
    @Override
    void remove();
}
