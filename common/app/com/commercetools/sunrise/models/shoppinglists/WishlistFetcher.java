package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@FunctionalInterface
@ImplementedBy(DefaultWishlistFetcher.class)
public interface WishlistFetcher extends ResourceFetcher<ShoppingList>, Supplier<CompletionStage<Optional<ShoppingList>>> {
    /**
     * If the current session contains a signed in customer or a previously created wishlist, this wishlist
     * will be returned. Otherwise an empty optional will be returned.
     *
     * @return the completion stage for the wishlist
     */
    CompletionStage<Optional<ShoppingList>> get();
}
