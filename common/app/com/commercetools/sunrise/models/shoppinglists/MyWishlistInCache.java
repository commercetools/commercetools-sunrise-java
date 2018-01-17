package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.sessions.ResourceInCache;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(MyWishlistInCacheImpl.class)
public interface MyWishlistInCache extends ResourceInCache<ShoppingList> {

    @Override
    CompletionStage<Optional<ShoppingList>> get();
}
