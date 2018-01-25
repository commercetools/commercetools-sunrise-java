package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.ResourceCreator;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyWishlistCreator.class)
public interface MyWishlistCreator extends ResourceCreator {

    CompletionStage<ShoppingList> get();
}
