package com.commercetools.sunrise.framework;

import com.commercetools.sunrise.framework.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@ImplementedBy(CartFinderBySession.class)
@FunctionalInterface
public interface CartFinder extends ResourceFinder, Supplier<CompletionStage<Optional<Cart>>> {

    @Override
    CompletionStage<Optional<Cart>> get();
}
