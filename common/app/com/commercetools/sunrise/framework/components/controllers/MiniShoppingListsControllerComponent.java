package com.commercetools.sunrise.framework.components.controllers;

import com.commercetools.sunrise.framework.hooks.application.PageDataReadyHook;
import com.commercetools.sunrise.framework.viewmodels.PageData;
import com.commercetools.sunrise.framework.viewmodels.content.wishlist.ShoppinglistViewModelFactory;
import com.commercetools.sunrise.sessions.wishlist.ShoppingListsInSession;
import com.google.inject.Inject;

public final class MiniShoppingListsControllerComponent implements ControllerComponent, PageDataReadyHook {
    private final ShoppingListsInSession shoppingListsInSession;
    private final ShoppinglistViewModelFactory shoppinglistViewModelFactory;

    @Inject
    protected MiniShoppingListsControllerComponent(final ShoppingListsInSession shoppingListsInSession,
                                                   final ShoppinglistViewModelFactory shoppinglistViewModelFactory) {
        this.shoppingListsInSession = shoppingListsInSession;
        this.shoppinglistViewModelFactory = shoppinglistViewModelFactory;
    }

    @Override
    public void onPageDataReady(final PageData pageData) {
        shoppingListsInSession.getShoppingListNames()
                .stream()
                .forEach(
                        shoppinglistName ->
                                pageData.getContent().put(shoppinglistName , shoppingListsInSession.findShoppingList(shoppinglistName)
                        .orElseGet(() -> shoppinglistViewModelFactory.create(null))));

    }
}
