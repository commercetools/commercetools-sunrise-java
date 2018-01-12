package com.commercetools.sunrise.wishlist.clear;

import com.commercetools.sunrise.core.controllers.FormAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
@ImplementedBy(DefaultClearWishlistFormAction.class)
public interface ClearWishlistFormAction extends FormAction {

    CompletionStage<ShoppingList> apply();
}
