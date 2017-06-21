package com.commercetools.sunrise.shoppingcart.remove;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultRemoveFromCartControllerAction.class)
@FunctionalInterface
public interface RemoveFromCartControllerAction extends ControllerAction, BiFunction<Cart, RemoveFromCartFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(final Cart cart, final RemoveFromCartFormData formData);
}
