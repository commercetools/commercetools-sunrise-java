package com.commercetools.sunrise.wishlist.clear;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@FunctionalInterface
@ImplementedBy(DefaultClearWishlistControllerAction.class)
public interface ClearWishlistControllerAction extends ControllerAction, Function<ShoppingList, CompletionStage<ShoppingList>> {
    /**
     * Removes all line items from the given wishlist.
     *
     * @param wishlist the {@link ShoppingList} from which to remove all line items
     *
     * @return the completion stage for the updated wishlist with no line items
     */
    @Override
    CompletionStage<ShoppingList> apply(ShoppingList wishlist);
}
