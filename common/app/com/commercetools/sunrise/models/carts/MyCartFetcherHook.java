package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.queries.CartQuery;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface MyCartFetcherHook extends FilterHook {

    CompletionStage<Optional<Cart>> on(CartQuery request, Function<CartQuery, CompletionStage<Optional<Cart>>> nextComponent);
}