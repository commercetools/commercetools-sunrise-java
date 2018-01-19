package com.commercetools.sunrise.core.hooks.ctprequests;

import com.commercetools.sunrise.core.hooks.FilterHook;
import com.commercetools.sunrise.core.hooks.HookRunner;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartUpdateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public interface CartUpdateCommandHook extends FilterHook {

    CompletionStage<Cart> on(CartUpdateCommand request, Function<CartUpdateCommand, CompletionStage<Cart>> nextComponent);

    static CompletionStage<Cart> run(final HookRunner hookRunner, final CartUpdateCommand request, final Function<CartUpdateCommand, CompletionStage<Cart>> execution) {
        return hookRunner.run(CartUpdateCommandHook.class, request, execution, h -> h::on);
    }
}
