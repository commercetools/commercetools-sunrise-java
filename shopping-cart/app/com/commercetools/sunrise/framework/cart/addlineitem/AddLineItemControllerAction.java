package com.commercetools.sunrise.framework.cart.addlineitem;

import com.commercetools.sunrise.framework.controllers.ControllerAction;
import com.google.inject.ImplementedBy;
import io.sphere.sdk.carts.Cart;

import java.util.concurrent.CompletionStage;
import java.util.function.BiFunction;

@ImplementedBy(DefaultAddLineItemControllerAction.class)
@FunctionalInterface
public interface AddLineItemControllerAction extends ControllerAction, BiFunction<Cart, AddLineItemFormData, CompletionStage<Cart>> {

    @Override
    CompletionStage<Cart> apply(final Cart cart, final AddLineItemFormData formData);
}
