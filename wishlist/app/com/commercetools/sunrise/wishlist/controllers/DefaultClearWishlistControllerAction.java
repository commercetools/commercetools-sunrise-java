package com.commercetools.sunrise.wishlist.controllers;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;
import io.sphere.sdk.shoppinglists.commands.updateactions.RemoveLineItem;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class DefaultClearWishlistControllerAction extends AbstractShoppingListUpdateExecutor implements ClearWishlistControllerAction {
    @Inject
    protected DefaultClearWishlistControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<ShoppingList> apply(final ShoppingList wishlist) {
        return executeRequest(wishlist, buildRequest(wishlist));
    }

    protected ShoppingListUpdateCommand buildRequest(final ShoppingList wishlist) {
        final List<RemoveLineItem> removeLineItems = wishlist.getLineItems().stream()
                .map(RemoveLineItem::of)
                .collect(Collectors.toList());

        return ShoppingListUpdateCommand.of(wishlist, removeLineItems);
    }
}
