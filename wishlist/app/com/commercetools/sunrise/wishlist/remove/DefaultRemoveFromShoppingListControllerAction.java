package com.commercetools.sunrise.wishlist.remove;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.wishlist.AbstractShoppingListUpdateExecutor;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;
import io.sphere.sdk.shoppinglists.commands.updateactions.RemoveLineItem;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultRemoveFromShoppingListControllerAction extends AbstractShoppingListUpdateExecutor implements RemoveFromShoppingListControllerAction {
    @Inject
    protected DefaultRemoveFromShoppingListControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<ShoppingList> apply(final ShoppingList shoppingList, final RemoveFromShoppingListFormData removeFromShoppingListFormData) {
        return executeRequest(shoppingList, buildRequest(shoppingList, removeFromShoppingListFormData));
    }

    protected ShoppingListUpdateCommand buildRequest(final ShoppingList shoppingList, final RemoveFromShoppingListFormData removeFromShoppingListFormData) {
        final RemoveLineItem removeLineItem = RemoveLineItem.of(removeFromShoppingListFormData.lineItemId());
        return ShoppingListUpdateCommand.of(shoppingList, removeLineItem);
    }
}
