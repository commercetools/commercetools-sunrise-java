package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListCreatedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListLoadedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListUpdatedHook;
import com.google.inject.Inject;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class MyWishlistStoringComponent implements ControllerComponent, ShoppingListCreatedHook, ShoppingListUpdatedHook, ShoppingListLoadedHook {

    private final MyWishlistInSession myWishlistInSession;
    private final MyWishlistInCache myWishlistInCache;

    @Inject
    MyWishlistStoringComponent(final MyWishlistInSession myWishlistInSession, final MyWishlistInCache myWishlistInCache) {
        this.myWishlistInSession = myWishlistInSession;
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
        myWishlistInSession.store(shoppingList);
        myWishlistInCache.store(shoppingList);
        return completedFuture(null);
    }
}
