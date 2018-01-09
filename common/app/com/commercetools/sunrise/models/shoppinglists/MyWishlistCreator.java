package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.controllers.ResourceCreator;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.ShoppingListDraft;
import play.libs.concurrent.HttpExecution;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyWishlistCreator.class)
public interface MyWishlistCreator extends ResourceCreator<ShoppingList, ShoppingListDraft> {

    CompletionStage<ShoppingListDraft> defaultDraft();

    default CompletionStage<ShoppingList> get() {
        return defaultDraft().thenComposeAsync(this::get, HttpExecution.defaultContext());
    }
}
