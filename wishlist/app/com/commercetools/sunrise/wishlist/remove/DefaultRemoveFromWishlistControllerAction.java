package com.commercetools.sunrise.wishlist.remove;

import com.commercetools.sunrise.models.shoppinglists.MyWishlistUpdater;
import io.sphere.sdk.shoppinglists.ShoppingList;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class DefaultRemoveFromWishlistControllerAction implements RemoveFromWishlistControllerAction {

    private final MyWishlistUpdater myWishlistUpdater;

    @Inject
    protected DefaultRemoveFromWishlistControllerAction(final MyWishlistUpdater myWishlistUpdater) {
        this.myWishlistUpdater = myWishlistUpdater;
    }

    @Override
    public CompletionStage<ShoppingList> apply(final RemoveFromWishlistFormData removeWishlistFormData) {
        return myWishlistUpdater.force(removeWishlistFormData.updateActions());
    }
}
