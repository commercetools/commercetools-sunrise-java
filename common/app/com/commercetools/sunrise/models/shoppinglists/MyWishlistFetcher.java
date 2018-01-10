package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.controllers.SingleResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyWishlistFetcher.class)
public interface MyWishlistFetcher extends SingleResourceFetcher<ShoppingList, ShoppingListQuery> {

    Optional<ShoppingListQuery> defaultRequest();

    CompletionStage<Optional<ShoppingList>> get();

    default CompletionStage<ShoppingList> require() {
        return get().thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
