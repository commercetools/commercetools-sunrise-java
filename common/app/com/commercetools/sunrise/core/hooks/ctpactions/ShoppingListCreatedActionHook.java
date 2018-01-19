package com.commercetools.sunrise.core.hooks.ctpactions;

import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.expansion.ExpansionPathContainer;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

public interface ShoppingListCreatedActionHook extends CtpActionHook {

    CompletionStage<ShoppingList> onShoppingListCreatedAction(final ShoppingList shoppingList, final ExpansionPathContainer<ShoppingList> expansionPathContainer);

    static CompletionStage<ShoppingList> runHook(final HookRunner hookRunner, final ShoppingList shoppingList, final ExpansionPathContainer<ShoppingList> expansionPathContainer) {
        return hookRunner.run(ShoppingListCreatedActionHook.class, shoppingList, (hook, createdShoppingList) -> hook.onShoppingListCreatedAction(createdShoppingList, expansionPathContainer));
    }

}
