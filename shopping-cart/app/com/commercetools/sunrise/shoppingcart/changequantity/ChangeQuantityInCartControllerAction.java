package com.commercetools.sunrise.shoppingcart.changequantity;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultChangeQuantityInCartControllerAction.class)
@FunctionalInterface
public interface ChangeQuantityInCartControllerAction extends ControllerAction, BiFunction<Cart, ChangeQuantityInCartFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(final Cart cart, final ChangeQuantityInCartFormData formData);
}
