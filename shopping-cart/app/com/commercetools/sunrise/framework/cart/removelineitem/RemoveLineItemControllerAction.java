package com.commercetools.sunrise.framework.cart.removelineitem;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultRemoveLineItemControllerAction.class)
@FunctionalInterface
public interface RemoveLineItemControllerAction extends ControllerAction, BiFunction<Cart, RemoveLineItemFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(final Cart cart, final RemoveLineItemFormData formData);
}
