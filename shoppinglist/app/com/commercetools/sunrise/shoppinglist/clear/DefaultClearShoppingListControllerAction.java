package com.commercetools.sunrise.shoppinglist.clear;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.shoppinglist.AbstractShoppingListUpdateExecutor;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;
import io.sphere.sdk.shoppinglists.commands.updateactions.RemoveLineItem;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class DefaultClearShoppingListControllerAction extends AbstractShoppingListUpdateExecutor implements ClearShoppingListControllerAction {
    @Inject
    protected DefaultClearShoppingListControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<ShoppingList> apply(final ShoppingList shoppingList) {
        return executeRequest(shoppingList, buildRequest(shoppingList));
    }

    protected ShoppingListUpdateCommand buildRequest(final ShoppingList wishlist) {
        final List<RemoveLineItem> removeLineItems = wishlist.getLineItems().stream()
                .map(RemoveLineItem::of)
                .collect(Collectors.toList());

        return ShoppingListUpdateCommand.of(wishlist, removeLineItems);
    }
}
