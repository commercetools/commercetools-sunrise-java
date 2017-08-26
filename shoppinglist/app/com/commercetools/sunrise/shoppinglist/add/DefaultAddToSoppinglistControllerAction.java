package com.commercetools.sunrise.shoppinglist.add;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.shoppinglist.AbstractShoppingListUpdateExecutor;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;
import io.sphere.sdk.shoppinglists.commands.updateactions.AddLineItem;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultAddToSoppinglistControllerAction extends AbstractShoppingListUpdateExecutor implements AddToShoppingListControllerAction {
    @Inject
    protected DefaultAddToSoppinglistControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<ShoppingList> apply(final ShoppingList shoppingList, final AddToShoppingListFormData addToShoppingListFormData) {
        return executeRequest(shoppingList, buildRequest(shoppingList, addToShoppingListFormData));
    }

    protected ShoppingListUpdateCommand buildRequest(final ShoppingList shoppingList, final AddToShoppingListFormData addToShoppingListFormData) {
        final AddLineItem addLineItem = AddLineItem.of(addToShoppingListFormData.productId()).withVariantId(addToShoppingListFormData.variantId());
        return ShoppingListUpdateCommand.of(shoppingList, addLineItem);
    }
}
