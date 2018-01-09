package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListCreatedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListLoadedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListUpdatedHook;
import com.commercetools.sunrise.models.shoppinglists.MyWishlistInCache;
import com.commercetools.sunrise.models.shoppinglists.WishlistInSession;
import com.google.inject.Inject;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class WishlistStoringComponent implements ControllerComponent, ShoppingListCreatedHook, ShoppingListUpdatedHook, ShoppingListLoadedHook {

    private final WishlistInSession wishlistInSession;
    private final MyWishlistInCache myWishlistInCache;

    @Inject
    WishlistStoringComponent(final WishlistInSession wishlistInSession, final MyWishlistInCache myWishlistInCache) {
        this.wishlistInSession = wishlistInSession;
        this.myWishlistInCache = myWishlistInCache;
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
        myWishlistInCache.store(shoppingList);
        return completedFuture(null);
    }
}
