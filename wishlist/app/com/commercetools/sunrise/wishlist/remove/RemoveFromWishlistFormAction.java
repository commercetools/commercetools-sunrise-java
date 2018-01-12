package com.commercetools.sunrise.wishlist.remove;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
@ImplementedBy(DefaultRemoveFromWishlistFormAction.class)
public interface RemoveFromWishlistFormAction extends FormAction {

    CompletionStage<ShoppingList> apply(RemoveFromWishlistFormData removeWishlistFormData);
}
