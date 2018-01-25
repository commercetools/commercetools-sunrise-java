package com.commercetools.sunrise.models.carts;

import com.commercetools.sunrise.core.hooks.FilterHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface MyCartUpdaterHook extends FilterHook {

    CompletionStage<Cart> on(CartUpdateCommand request, Function<CartUpdateCommand, CompletionStage<Cart>> nextComponent);
}
