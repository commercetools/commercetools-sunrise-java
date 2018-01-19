package com.commercetools.sunrise.core.hooks.ctpevents;

import com.commercetools.sunrise.core.hooks.ConsumerHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.ShoppingList;

public interface ShoppingListCreatedHook extends ConsumerHook {

    void onCreated(ShoppingList shoppingList);

    static void run(final HookRunner hookRunner, final ShoppingList resource) {
        hookRunner.run(ShoppingListCreatedHook.class, h -> h.onCreated(resource));
    }
}
