package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyWishlistFetcher.class)
public interface MyWishlistFetcher extends ResourceFetcher {

    CompletionStage<Optional<ShoppingList>> get();

    default CompletionStage<ShoppingList> require() {
        return get().thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
