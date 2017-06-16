package com.commercetools.sunrise.wishlist.controllers;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.wishlist.viewmodels.RemoveWishlistLineItemFormData;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;
import io.sphere.sdk.shoppinglists.commands.updateactions.RemoveLineItem;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultRemoveFromWishlistControllerAction extends AbstractShoppingListUpdateExecutor implements RemoveFromWishlistControllerAction {
    @Inject
    protected DefaultRemoveFromWishlistControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<ShoppingList> apply(final ShoppingList wishlist, final RemoveWishlistLineItemFormData removeWishlistFormData) {
        return executeRequest(wishlist, buildRequest(wishlist, removeWishlistFormData));
    }

    protected ShoppingListUpdateCommand buildRequest(final ShoppingList wishlist, final RemoveWishlistLineItemFormData removeWishlistFormData) {
        final RemoveLineItem removeLineItem = RemoveLineItem.of(removeWishlistFormData.lineItemId());
        return ShoppingListUpdateCommand.of(wishlist, removeLineItem);
    }
}
