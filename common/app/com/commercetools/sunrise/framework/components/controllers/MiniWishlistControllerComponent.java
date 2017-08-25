package com.commercetools.sunrise.framework.components.controllers;

import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.framework.viewmodels.content.wishlist.ShoppinglistViewModel;
import com.commercetools.sunrise.framework.viewmodels.content.wishlist.ShoppinglistViewModelFactory;
import com.commercetools.sunrise.sessions.wishlist.ShoppingListsInSession;
import com.google.inject.Inject;

public final class MiniWishlistControllerComponent implements ControllerComponent, PageDataReadyHook {
    private final ShoppingListsInSession shoppingListsInSession;
    private final ShoppinglistViewModelFactory shoppinglistViewModelFactory;

    @Inject
    protected MiniWishlistControllerComponent(final ShoppingListsInSession shoppingListsInSession,
                                              final ShoppinglistViewModelFactory shoppinglistViewModelFactory) {
        this.shoppingListsInSession = shoppingListsInSession;
        this.shoppinglistViewModelFactory = shoppinglistViewModelFactory;
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        final ShoppinglistViewModel wishlist = shoppingListsInSession.findShoppingList("wishlist")
                .orElseGet(() -> shoppinglistViewModelFactory.create(null));

        pageData.getContent().put("wishlist", wishlist); // we use a dynamic property here, because we don't want to extend PageContent
    }
}
