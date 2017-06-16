package com.commercetools.sunrise.wishlist.controllers;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.ctpevents.ShoppingListCreatedHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.ShoppingListLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.ShoppingListUpdatedHook;
import com.commercetools.sunrise.sessions.wishlist.WishlistInSession;
import com.google.inject.Inject;
import io.sphere.sdk.shoppinglists.ShoppingList;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class WishlistInSessionControllerComponent implements ControllerComponent,
        ShoppingListCreatedHook, ShoppingListUpdatedHook, ShoppingListLoadedHook {
    private final WishlistInSession wishlistInSession;

    @Inject
    protected WishlistInSessionControllerComponent(final WishlistInSession wishlistInSession) {
        this.wishlistInSession = wishlistInSession;
    }

    @Override
    public CompletionStage<?> onShoppingListLoaded(final ShoppingList shoppingList) {
        overwriteWishlistInSession(shoppingList);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onShoppingListCreated(final ShoppingList shoppingList) {
        overwriteWishlistInSession(shoppingList);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onShoppingListUpdated(final ShoppingList shoppingList) {
        overwriteWishlistInSession(shoppingList);
        return completedFuture(null);
    }

    private void overwriteWishlistInSession(@Nullable final ShoppingList wishlist) {
        wishlistInSession.store(wishlist);
    }
}
