package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.core.components.controllers.ControllerComponent;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListCreatedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListLoadedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListUpdatedHook;
import com.commercetools.sunrise.models.wishlists.WishlistInSession;
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
