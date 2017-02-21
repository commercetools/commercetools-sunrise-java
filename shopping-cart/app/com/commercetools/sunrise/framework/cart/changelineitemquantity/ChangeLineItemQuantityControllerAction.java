package com.commercetools.sunrise.framework.cart.changelineitemquantity;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultChangeLineItemQuantityControllerAction.class)
@FunctionalInterface
public interface ChangeLineItemQuantityControllerAction extends ControllerAction, BiFunction<Cart, ChangeLineItemQuantityFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(final Cart cart, final ChangeLineItemQuantityFormData formData);
}
