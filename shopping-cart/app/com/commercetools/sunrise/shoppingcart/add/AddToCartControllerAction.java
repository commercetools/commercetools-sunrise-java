package com.commercetools.sunrise.shoppingcart.add;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultAddToCartControllerAction.class)
@FunctionalInterface
public interface AddToCartControllerAction extends ControllerAction, BiFunction<Cart, AddToCartFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(final Cart cart, final AddToCartFormData formData);
}
