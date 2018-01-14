package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.ShoppingList;

public interface ShoppingListCreatedHook extends CtpEventHook {

    void onShoppingListCreated(ShoppingList shoppingList);

    static void runHook(final HookRunner hookRunner, final ShoppingList shoppingList) {
        hookRunner.runEventHook(ShoppingListCreatedHook.class, hook -> hook.onShoppingListCreated(shoppingList));
    }

}
