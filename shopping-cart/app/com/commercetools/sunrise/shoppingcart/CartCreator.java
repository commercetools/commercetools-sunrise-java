package com.commercetools.sunrise.shoppingcart;

import com.commercetools.sunrise.framework.controllers.ResourceFinder;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;

@ImplementedBy(DefaultCartCreator.class)
@FunctionalInterface
public interface CartCreator extends ResourceFinder, Supplier<CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> get();
}
