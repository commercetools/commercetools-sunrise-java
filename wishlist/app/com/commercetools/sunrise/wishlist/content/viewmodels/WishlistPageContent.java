package com.commercetools.sunrise.wishlist.content.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContent;
import io.sphere.sdk.shoppinglists.ShoppingList;

/**
 * The page content for the wishlist.
 */
public class WishlistPageContent extends PageContent {
    private ShoppingList wishlist;

    public ShoppingList getWishlist() {
        return wishlist;
    }

    public void setWishlist(ShoppingList wishlist) {
        this.wishlist = wishlist;
    }
}
