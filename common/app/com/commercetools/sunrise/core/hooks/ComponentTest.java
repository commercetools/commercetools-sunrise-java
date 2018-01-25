package com.commercetools.sunrise.core.hooks;

import com.commercetools.sunrise.core.components.ControllerComponent;
import com.commercetools.sunrise.models.carts.MyCartCreatorHook;
import io.sphere.sdk.carts.Cart;
import io.sphere.sdk.carts.commands.CartCreateCommand;

import java.util.concurrent.CompletionStage;
import java.util.function.Function;

public class ComponentTest implements ControllerComponent, MyCartCreatorHook {

    @Override
    public CompletionStage<Cart> on(final CartCreateCommand command, final Function<CartCreateCommand, CompletionStage<Cart>> nextComponent) {
        return nextComponent.apply(command);
    }
}
