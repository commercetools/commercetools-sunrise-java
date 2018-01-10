package com.commercetools.sunrise.wishlist.clear;

import com.commercetools.sunrise.core.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

@FunctionalInterface
@ImplementedBy(DefaultClearWishlistControllerAction.class)
public interface ClearWishlistControllerAction extends ControllerAction {

    CompletionStage<ShoppingList> apply();
}
