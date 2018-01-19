package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartCreateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

@FunctionalInterface
public interface CartCreateCommandHook extends FilterHook {

    CompletionStage<Cart> on(CartCreateCommand request, Function<CartCreateCommand, CompletionStage<Cart>> nextComponent);

    static CompletionStage<Cart> run(final HookRunner hookRunner, final CartCreateCommand request, final Function<CartCreateCommand, CompletionStage<Cart>> execution) {
        return hookRunner.run(CartCreateCommandHook.class, request, execution, h -> h::on);
    }
}
