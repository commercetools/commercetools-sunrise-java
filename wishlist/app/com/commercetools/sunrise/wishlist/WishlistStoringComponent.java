package com.commercetools.sunrise.wishlist;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListCreatedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListLoadedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListUpdatedHook;
import com.commercetools.sunrise.models.shoppinglists.WishlistInCache;
import com.commercetools.sunrise.models.shoppinglists.WishlistInSession;
import com.google.inject.Inject;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class WishlistStoringComponent implements ControllerComponent, ShoppingListCreatedHook, ShoppingListUpdatedHook, ShoppingListLoadedHook {

    private final WishlistInSession wishlistInSession;
    private final WishlistInCache wishlistInCache;

    @Inject
    WishlistStoringComponent(final WishlistInSession wishlistInSession, final WishlistInCache wishlistInCache) {
        this.wishlistInSession = wishlistInSession;
        this.wishlistInCache = wishlistInCache;
    }

    @Override
    public CompletionStage<?> onShoppingListLoaded(final ShoppingList shoppingList) {
        return storeWishlist(shoppingList);
    }

    @Override
    public CompletionStage<?> onShoppingListCreated(final ShoppingList shoppingList) {
        return storeWishlist(shoppingList);
    }

    @Override
    public CompletionStage<?> onShoppingListUpdated(final ShoppingList shoppingList) {
        return storeWishlist(shoppingList);
    }

    private CompletionStage<?> storeWishlist(final ShoppingList shoppingList) {
        wishlistInSession.store(shoppingList);
        wishlistInCache.store(shoppingList);
        return completedFuture(null);
    }
}
