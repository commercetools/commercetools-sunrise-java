package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.sessions.UserResourceInCache;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(MyWishlistImpl.class)
public interface MyWishlist extends UserResourceInCache<ShoppingList> {

    @Override
    CompletionStage<Optional<ShoppingList>> get();
}
