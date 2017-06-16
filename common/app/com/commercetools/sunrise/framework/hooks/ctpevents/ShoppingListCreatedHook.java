package com.commercetools.sunrise.framework.hooks.ctpevents;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;


public interface ShoppingListCreatedHook extends CtpEventHook {

    CompletionStage<?> onShoppingListCreated(ShoppingList shoppingList);

    static CompletionStage<?> runHook(final HookRunner hookRunner, final ShoppingList shoppingList) {
        return hookRunner.runEventHook(ShoppingListCreatedHook.class, hook -> hook.onShoppingListCreated(shoppingList));
    }

}
