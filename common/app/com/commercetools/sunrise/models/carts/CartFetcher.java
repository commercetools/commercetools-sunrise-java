package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.controllers.ResourceFetcher;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@ImplementedBy(DefaultCartFetcher.class)
@FunctionalInterface
public interface CartFetcher extends ResourceFetcher<Cart>, Supplier<CompletionStage<Optional<Cart>>> {

    @Override
    CompletionStage<Optional<Cart>> get();
}
