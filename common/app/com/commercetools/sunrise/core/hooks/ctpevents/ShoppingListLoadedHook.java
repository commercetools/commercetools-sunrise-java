package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

public interface ShoppingListLoadedHook extends CtpEventHook {

    void onShoppingListLoaded(ShoppingList shoppingList);

    static void runHook(final HookRunner hookRunner, final ShoppingList shoppingList) {
        hookRunner.runEventHook(ShoppingListLoadedHook.class, hook -> hook.onShoppingListLoaded(shoppingList));
    }

}
