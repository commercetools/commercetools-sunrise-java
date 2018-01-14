package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.core.hooks.application.PageDataHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListCreatedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListLoadedHook;
import com.commercetools.sunrise.core.hooks.ctpevents.ShoppingListUpdatedHook;
import com.commercetools.sunrise.core.viewmodels.PageData;
import com.google.inject.Inject;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

public final class MyWishlistComponent implements ControllerComponent, ShoppingListCreatedHook, ShoppingListUpdatedHook, ShoppingListLoadedHook, PageDataHook {

    private final MyWishlistInSession myWishlistInSession;
    private final MyWishlistInCache myWishlistInCache;

    @Inject
    MyWishlistComponent(final MyWishlistInSession myWishlistInSession, final MyWishlistInCache myWishlistInCache) {
        this.myWishlistInSession = myWishlistInSession;
        this.myWishlistInCache = myWishlistInCache;
    }

    @Override
    public void onShoppingListLoaded(final ShoppingList shoppingList) {
        storeWishlist(shoppingList);
    }

    @Override
    public void onShoppingListCreated(final ShoppingList shoppingList) {
        storeWishlist(shoppingList);
    }

    @Override
    public void onShoppingListUpdated(final ShoppingList shoppingList) {
        storeWishlist(shoppingList);
    }

    @Override
    public CompletionStage<PageData> onPageDataReady(final PageData pageData) {
        return myWishlistInCache.get()
                .thenApply(wishlistOpt -> wishlistOpt
                        .map(wishlist -> pageData.put("wishlist", wishlist))
                        .orElse(pageData));
    }

    private void storeWishlist(final ShoppingList shoppingList) {
        myWishlistInSession.store(shoppingList);
        myWishlistInCache.store(shoppingList);
    }
}
