package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.ShoppingList;

public interface ShoppingListUpdatedHook extends CtpEventHook {

    void onShoppingListUpdated(ShoppingList shoppingList);

    static void runHook(final HookRunner hookRunner, final ShoppingList shoppingList) {
        hookRunner.runEventHook(ShoppingListUpdatedHook.class, hook -> hook.onShoppingListUpdated(shoppingList));
    }

}
