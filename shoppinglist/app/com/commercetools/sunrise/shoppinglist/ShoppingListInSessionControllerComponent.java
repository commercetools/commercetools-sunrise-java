package com.commercetools.sunrise.shoppinglist;

import com.commercetools.sunrise.framework.components.controllers.ControllerComponent;
import com.commercetools.sunrise.framework.hooks.ctpevents.ShoppingListCreatedHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.ShoppingListLoadedHook;
import com.commercetools.sunrise.framework.hooks.ctpevents.ShoppingListUpdatedHook;
import com.commercetools.sunrise.sessions.wishlist.ShoppingListsInSession;
import com.google.inject.Inject;
import io.sphere.sdk.shoppinglists.ShoppingList;

import javax.annotation.Nullable;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.completedFuture;

public final class ShoppingListInSessionControllerComponent implements ControllerComponent,
        ShoppingListCreatedHook, ShoppingListUpdatedHook, ShoppingListLoadedHook {
    private final ShoppingListsInSession shoppingListsInSession;

    @Inject
    protected ShoppingListInSessionControllerComponent(final ShoppingListsInSession shoppingListsInSession) {
        this.shoppingListsInSession = shoppingListsInSession;
    }

    @Override
    public CompletionStage<?> onShoppingListLoaded(final ShoppingList shoppingList) {
        overwriteShoppinglistInSession(shoppingList);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onShoppingListCreated(final ShoppingList shoppingList) {
        overwriteShoppinglistInSession(shoppingList);
        return completedFuture(null);
    }

    @Override
    public CompletionStage<?> onShoppingListUpdated(final ShoppingList shoppingList) {
        overwriteShoppinglistInSession(shoppingList);
        return completedFuture(null);
    }

    private void overwriteShoppinglistInSession(@Nullable final ShoppingList shoppingList) {
        shoppingListsInSession.store(shoppingList);
    }
}
