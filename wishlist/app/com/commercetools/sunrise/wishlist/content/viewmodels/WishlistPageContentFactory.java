package com.commercetools.sunrise.wishlist.content.viewmodels;

import com.commercetools.sunrise.core.viewmodels.content.PageContentFactory;
import io.sphere.sdk.shoppinglists.ShoppingList;

/**
 * The factory class for creating {@link WishlistPageContent}.
 */
public class WishlistPageContentFactory extends PageContentFactory<WishlistPageContent, ShoppingList> {

    @Override
    protected WishlistPageContent newViewModelInstance(final ShoppingList input) {
        return new WishlistPageContent();
    }

    @Override
    public final WishlistPageContent create(final ShoppingList wishlist) {
        return super.create(wishlist);
    }

    @Override
    protected final void initialize(final WishlistPageContent viewModel, final ShoppingList wishlist) {
        super.initialize(viewModel, wishlist);
        fillWishlist(viewModel, wishlist);
    }

    protected void fillWishlist(final WishlistPageContent viewModel, final ShoppingList wishlist) {
        viewModel.setWishlist(wishlist);
    }
}
