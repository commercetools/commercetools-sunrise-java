package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.ShoppingList;

public interface ShoppingListUpdatedHook extends ConsumerHook {

    void onUpdated(ShoppingList shoppingList);

    static void run(final HookRunner hookRunner, final ShoppingList resource) {
        hookRunner.run(ShoppingListUpdatedHook.class, h -> h.onUpdated(resource));
    }
}
