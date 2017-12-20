package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.core.components.controllers.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.commercetools.sunrise.models.wishlists.WishlistInSession;
import com.google.inject.Inject;
import io.sphere.sdk.shoppinglists.ShoppingList;

public final class MiniWishlistControllerComponent implements ControllerComponent, PageDataReadyHook {
    private final WishlistInSession wishlistInSession;

    @Inject
    protected MiniWishlistControllerComponent(final WishlistInSession wishlistInSession) {
        this.wishlistInSession = wishlistInSession;
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        ShoppingList value = wishlistInSession.findWishlist().orElse(null);
        pageData.getContent().put("wishlist", value); // we use a dynamic property here, because we don't want to extend PageContent
    }
}
