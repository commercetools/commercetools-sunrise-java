package com.commercetools.sunrise.core.hooks.ctpactions;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

public interface ShoppingListUpdatedActionHook extends CtpActionHook {

    CompletionStage<ShoppingList> onShoppingListUpdatedAction(final ShoppingList shoppingList, final ExpansionPathContainer<ShoppingList> expansionPathContainer);

    static CompletionStage<ShoppingList> runHook(final HookRunner hookRunner, final ShoppingList shoppingList, final ExpansionPathContainer<ShoppingList> expansionPathContainer) {
        return hookRunner.run(ShoppingListUpdatedActionHook.class, shoppingList, (hook, updatedShoppingList) -> hook.onShoppingListUpdatedAction(updatedShoppingList, expansionPathContainer));
    }

}
