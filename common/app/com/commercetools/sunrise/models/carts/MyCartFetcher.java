package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.NotFoundResourceException;
import com.commercetools.sunrise.core.SingleResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.queries.CartQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(DefaultMyCartFetcher.class)
public interface MyCartFetcher extends SingleResourceFetcher<Cart, CartQuery> {

    Optional<CartQuery> defaultRequest();

    CompletionStage<Optional<Cart>> get();

    default CompletionStage<Cart> require() {
        return get().thenApply(resourceOpt -> resourceOpt.orElseThrow(NotFoundResourceException::new));
    }
}
