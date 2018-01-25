package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartCreateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@FunctionalInterface
public interface MyCartCreatorHook extends FilterHook {

    CompletionStage<Cart> on(CartCreateCommand request, Function<CartCreateCommand, CompletionStage<Cart>> nextComponent);
}
