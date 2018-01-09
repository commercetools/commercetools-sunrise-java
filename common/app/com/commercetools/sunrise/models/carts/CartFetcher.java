package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.queries.CartQuery;
import io.sphere.sdk.queries.PagedQueryResult;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultCartFetcher.class)
public interface CartFetcher extends ResourceFetcher<Cart, CartQuery, PagedQueryResult<Cart>> {

    Optional<CartQuery> defaultRequest();

    CompletionStage<Optional<Cart>> get();

    default CompletionStage<Cart> require() {
        return get().thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
