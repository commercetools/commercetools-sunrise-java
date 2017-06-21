package com.commercetools.sunrise.wishlist.add;

import com.commercetools.sunrise.framework.hooks.HookRunner;
import com.commercetools.sunrise.wishlist.AbstractShoppingListUpdateExecutor;
import io.sphere.sdk.client.SphereClient;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.commands.ShoppingListUpdateCommand;
import io.sphere.sdk.shoppinglists.commands.updateactions.AddLineItem;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultAddToWishlistControllerAction extends AbstractShoppingListUpdateExecutor implements AddToWishlistControllerAction {
    @Inject
    protected DefaultAddToWishlistControllerAction(final SphereClient sphereClient, final HookRunner hookRunner) {
        super(sphereClient, hookRunner);
    }

    @Override
    public CompletionStage<ShoppingList> apply(final ShoppingList wishlist, final AddWishlistLineItemFormData addWishlistLineItemFormData) {
        return executeRequest(wishlist, buildRequest(wishlist, addWishlistLineItemFormData));
    }

    protected ShoppingListUpdateCommand buildRequest(final ShoppingList wishlist, final AddWishlistLineItemFormData addToWishlistFormData) {
        final AddLineItem addLineItem = AddLineItem.of(addToWishlistFormData.productId()).withVariantId(addToWishlistFormData.variantId());
        return ShoppingListUpdateCommand.of(wishlist, addLineItem);
    }
}
