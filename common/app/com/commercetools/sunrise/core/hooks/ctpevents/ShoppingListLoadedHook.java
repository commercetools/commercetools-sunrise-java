package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.ShoppingList;

@FunctionalInterface
public interface ShoppingListLoadedHook extends ConsumerHook {

    void onLoaded(ShoppingList shoppingList);

    static void run(final HookRunner hookRunner, final ShoppingList resource) {
        hookRunner.run(ShoppingListLoadedHook.class, h -> h.onLoaded(resource));
    }
}
