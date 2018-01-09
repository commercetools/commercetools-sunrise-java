package com.commercetools.sunrise.models.shoppinglists;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.queries.PagedQueryResult;
import io.sphere.sdk.shoppinglists.ShoppingList;
import io.sphere.sdk.shoppinglists.queries.ShoppingListQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultWishlistFetcher.class)
public interface WishlistFetcher extends ResourceFetcher<ShoppingList, ShoppingListQuery, PagedQueryResult<ShoppingList>> {

    Optional<ShoppingListQuery> defaultRequest();

    CompletionStage<Optional<ShoppingList>> get();

    default CompletionStage<ShoppingList> require() {
        return get().thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
